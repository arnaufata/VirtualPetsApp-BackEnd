package com.virtualpets.app.controllers;

import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.User;
import com.virtualpets.app.services.PetService;
import com.virtualpets.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Pet Controller", description = "Endpoints to handle pet interactions")
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet created successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createPet(@Parameter(description = "Pet details") @RequestBody Pet pet, Authentication authentication) {
        Optional<User> owner = userService.findByUsername(authentication.getName());
        if (owner.isPresent()) {
            petService.createPet(pet, owner.get());
            return ResponseEntity.ok("Successfully created pet");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @Operation(summary = "Get all pets", description = "Retrieve all pets associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of user's pets", content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets(Authentication authentication) {
        List<Pet> pets = petService.getPetsByUser(authentication.getName());
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Update pet details", description = "Update the details of a specific pet owned by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet updated successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_USER') and @petService.hasAccessToPet(#updatedPet, authentication)")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePet(
            @Parameter(description = "Pet ID") @PathVariable Long id,
            @Parameter(description = "Updated pet details") @RequestBody Pet petDetails,
            Authentication authentication) {
        boolean isUpdated = petService.updatePet(id, petDetails, authentication.getName());
        return isUpdated ? ResponseEntity.ok("Updated pet") : ResponseEntity.status(403).body("Access denied");
    }

    @Operation(summary = "Delete a pet", description = "Delete a pet owned by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_USER') and @petService.hasAccessToPet(#petId, authentication)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePet(@Parameter(description = "ID de la mascota") @PathVariable Long id,
                                            Authentication authentication) {
        boolean isDeleted = petService.deletePet(id, authentication.getName());
        return isDeleted ? ResponseEntity.ok("Pet deleted") : ResponseEntity.status(403).body("Access denied");
    }
}