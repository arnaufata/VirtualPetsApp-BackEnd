package com.virtualpets.app.services;

import com.virtualpets.app.exceptions.UserNotFoundException;
import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.User;
import com.virtualpets.app.repositories.PetRepository;
import com.virtualpets.app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public void createPet(Pet pet, User owner) {
        pet.setOwner(owner);
        petRepository.save(pet);
        logger.info("Mascota creada amb èxit per l'usuari: {}", owner.getUsername());
    }

    public List<Pet> getPetsByUser(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (user.getRoles().contains("ROLE_ADMIN")) {
                        // Administradors veuen totes les mascotes
                        return petRepository.findAll();
                    } else {
                        // Usuaris normals veuen només les seves mascotes
                        return petRepository.findByOwner(user);
                    }
                })
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    public boolean updatePet(Long petId, Pet petDetails, String username) {
        return petRepository.findById(petId).map(pet -> {
            if (isUserAllowedToModifyPet(pet, username)) {
                pet.setName(petDetails.getName());
                pet.setColor(petDetails.getColor());
                pet.setEnergyLevel(petDetails.getEnergyLevel());
                pet.setHungerLevel(petDetails.getHungerLevel());
                pet.setHappinessLevel(petDetails.getHappinessLevel());
                petRepository.save(pet);
                logger.info("Mascota actualitzada per l'usuari: {}", username);
                return true;
            }
            return false;
        }).orElse(false);
    }

    public boolean interactWithPet(Long petId, String action, String username) {
        return petRepository.findById(petId).map(pet -> {
            if (isUserAllowedToModifyPet(pet, username)) {
                switch (action.toLowerCase()) {
                    case "feed":
                        pet.feed();
                        break;
                    case "play":
                        pet.play();
                        break;
                    case "rest":
                        pet.rest();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown action: " + action);
                }
                petRepository.save(pet);
                logger.info("Mascota interactuada amb èxit. Acció: {} per l'usuari: {}", action, username);
                return true;
            }
            return false;
        }).orElse(false);
    }


    public boolean deletePet(Long petId, String username) {
        return petRepository.findById(petId).map(pet -> {
            if (isUserAllowedToModifyPet(pet, username)) {
                petRepository.delete(pet);
                logger.info("Mascota eliminada per l'usuari: {}", username);
                return true;
            }
            return false;
        }).orElse(false);
    }

    private boolean isUserAllowedToModifyPet(Pet pet, String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().contains("ROLE_ADMIN") || pet.getOwner().getUsername().equals(username))
                .orElse(false);
    }
}