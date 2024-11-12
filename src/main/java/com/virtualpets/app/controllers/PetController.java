package com.virtualpets.app.controllers;

import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.User;
import com.virtualpets.app.services.PetService;
import com.virtualpets.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    @Operation(summary = "Create a new pet", description = "Create a pet for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Pet created successfully")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @PostMapping("/create")
    public ResponseEntity<String> createPet(@RequestBody(description = "Pet details") Pet pet, Authentication authentication) {
        Optional<User> owner = userService.findByUsername(authentication.getName());
        if (owner.isPresent()) {
            petService.createPet(pet, owner.get());
            return ResponseEntity.ok("Successfully created pet");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @Operation(summary = "Get all pets", description = "Retrieve all pets associated with the authenticated user.")
    @ApiResponse(responseCode = "200", description = "List of user's pets", content = @Content(schema = @Schema(implementation = List.class)))
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets(Authentication authentication) {
        List<Pet> pets = petService.getPetsByUser(authentication.getName());
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Update pet details", description = "Update the details of a specific pet owned by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Pet updated successfully")
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePet(@PathVariable Long id, @RequestBody(description = "Updated pet details") Pet petDetails, Authentication authentication) {
        boolean isUpdated = petService.updatePet(id, petDetails, authentication.getName());
        return isUpdated ? ResponseEntity.ok("Updated pet") : ResponseEntity.status(403).body("Access denied");
    }

    @Operation(summary = "Delete a pet", description = "Delete a pet owned by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Pet deleted successfully")
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id, Authentication authentication) {
        boolean isDeleted = petService.deletePet(id, authentication.getName());
        return isDeleted ? ResponseEntity.ok("Pet deleted") : ResponseEntity.status(403).body("Access denied");
    }
}