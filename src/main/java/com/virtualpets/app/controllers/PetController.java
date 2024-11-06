package com.virtualpets.app.controllers;

import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.User;
import com.virtualpets.app.services.PetService;
import com.virtualpets.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final UserService userService;

    @Autowired
    public PetController(PetService petService, UserService userService) {
        this.petService  = petService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPet(@RequestBody Pet pet, Authentication authentication) {
        Optional<User> owner = userService.findByUsername(authentication.getName());
        if (owner.isPresent()) {
            petService.createPet(pet, owner.get());
            return ResponseEntity.ok("Successfully created pet");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets(Authentication authentication) {
        List<Pet> pets = petService.getPetsByUser(authentication.getName());
        return ResponseEntity.ok(pets);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePet(@PathVariable Long id, @RequestBody Pet petDetails, Authentication authentication) {
        boolean isUpdated = petService.updatePet(id, petDetails, authentication.getName());
        return isUpdated ? ResponseEntity.ok("Updated pet") : ResponseEntity.status(403).body("Access denied");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id, Authentication authentication) {
        boolean isDeleted = petService.deletePet(id, authentication.getName());
        return isDeleted ? ResponseEntity.ok("Pet deleted") : ResponseEntity.status(403).body("Access denied");
    }
}
