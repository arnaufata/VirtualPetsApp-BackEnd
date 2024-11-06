package com.virtualpets.app.services;

import com.virtualpets.app.exceptions.UserNotFoundException;
import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.PetType;
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
        this.petRepository  = petRepository;
        this.userRepository = userRepository;
    }

    // Crear mascota i associar-la amb el propietari
    public void createPet(Pet pet, User owner) {
        pet.setOwner(owner);
        petRepository.save(pet);
        logger.info("Mascota creada amb èxit per l'usuari: {}", owner.getUsername());
    }

    // Obtenir mascotes segons el rol de l'usuari
    public List<Pet> getPetsByUser(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().contains("ROLE_ADMIN") ? petRepository.findAll() : petRepository.findByOwner(user))
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    // Actualitzar mascota, només si l'usuari té permís
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

    // Eliminar mascota, només si l'usuari té permís
    public boolean deletePet(Long petId, String username) {
        return petRepository.findById(petId).map(pet ->  {
            if (isUserAllowedToModifyPet(pet, username)) {
                petRepository.delete(pet);
                logger.info("Mascota eliminada per l'usuari: {}", username);
                return true;
            }
            return false;
        }).orElse(false);
    }

    // Helper per verificar si l'usuari pot modificar o eliminar la mascota
    private boolean isUserAllowedToModifyPet(Pet pet, String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().contains("ROLE_ADMIN") || pet.getOwner().getUsername().equals(username))
                .orElse(false);
    }

    public void createDefaultPetsIfNotExists() {
        if (petRepository.count() == 0) {
            // Assignem les mascotes predeterminades a un propierari existent
            User owner = userRepository.findByUsername("admin").orElseThrow(
                    () -> new UserNotFoundException("Admin user not found to assign default pets to.")
            );

            // Creació de les mascotes amb l'usuari "admin" com a propietari
            Pet dragon = new Pet("DRAGON", PetType.DRAGON, "Red", owner);
            Pet unicorn = new Pet("UNICORN", PetType.UNICORN, "White", owner);
            Pet alien = new Pet("ALIEN", PetType.ALIEN, "Green", owner);
            Pet cat = new Pet("CAT", PetType.CAT, "Black", owner);

            petRepository.save(dragon);
            petRepository.save(unicorn);
            petRepository.save(alien);
            petRepository.save(cat);

            logger.info("Mascotes predeterminades creades amb èxit.");
        } else {
            logger.info("Ja existeixen mascotes a la base de dades. No es crearan mascotes predeterminades.");
        }
    }
}