package com.virtualpets.app.controllers;

import com.virtualpets.app.dto.PetCreateDTO;
import com.virtualpets.app.dto.PetUpdateDTO;
import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.User;
import com.virtualpets.app.services.PetService;
import com.virtualpets.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<String> createPet(
            @Parameter(description = "Pet creation details") @RequestBody PetCreateDTO petCreateDTO) {
        String username = getAuthenticatedUsername();
        Optional<User> owner = userService.findByUsername(username);
        if (owner.isPresent()) {
            Pet pet = new Pet();
            pet.setName(petCreateDTO.getName());
            pet.setType(petCreateDTO.getType());
            pet.setColor(petCreateDTO.getColor());
            pet.setOwner(owner.get());
            pet.setEnergyLevel(100);
            pet.setHungerLevel(0);
            pet.setHappinessLevel(100);

            petService.createPet(pet, owner.get());
            return ResponseEntity.status(201).body("Successfully created pet");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @Operation(summary = "Get all pets", description = "Retrieve all pets associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of user's pets", content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPets() {
        String username = getAuthenticatedUsername();
        List<Pet> pets = petService.getPetsByUser(username);

        // Afegim l'atribut imageUrl a cada mascota
        List<Map<String, Object>> petsWithImages = pets.stream().map(pet -> {
            Map<String, Object> petData = new HashMap<>();
            petData.put("id", pet.getId());
            petData.put("name", pet.getName());
            petData.put("type", pet.getType());
            petData.put("color", pet.getColor());
            petData.put("energyLevel", pet.getEnergyLevel());
            petData.put("hungerLevel", pet.getHungerLevel());
            petData.put("happinessLevel", pet.getHappinessLevel());
            petData.put("imageUrl", pet.getImageUrl()); // Genera la URL
            return petData;
        }).toList();

        return ResponseEntity.ok(petsWithImages);
    }

    @Operation(summary = "Update pet details", description = "Update the details of a specific pet owned by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet updated successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @petService.isUserAllowedToAccessPet(#id, authentication))")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePet(
            @Parameter(description = "Pet ID") @PathVariable Long id,
            @Parameter(description = "Updated pet details") @RequestBody PetUpdateDTO petUpdateDTO) {
        String username = getAuthenticatedUsername();
        boolean isUpdated = petService.updatePet(id, petUpdateDTO, username);
        return isUpdated ? ResponseEntity.ok("Pet updated successfully") : ResponseEntity.status(403).body("Access denied");
    }

    @Operation(summary = "Interact with a pet", description = "Allows the user to perform an action (feed, play, rest) on their pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet interaction successful", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @petService.isUserAllowedToAccessPet(#id, authentication))")
    @PostMapping("/interact/{id}")
    public ResponseEntity<String> interactWithPet(
            @Parameter(description = "Pet ID") @PathVariable Long id,
            @Parameter(description = "Action to perform (feed, play, rest)") @RequestParam String action) {
        String username = getAuthenticatedUsername();
        boolean isInteracted = petService.interactWithPet(id, action, username);
        return isInteracted ? ResponseEntity.ok("Interaction successful") : ResponseEntity.status(403).body("Access denied");
    }


    @Operation(summary = "Delete a pet", description = "Delete a pet owned by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @petService.isUserAllowedToAccessPet(#id, authentication))")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePet(@Parameter(description = "ID de la mascota") @PathVariable Long id) {
        String username = getAuthenticatedUsername();
        boolean isDeleted = petService.deletePet(id, username);
        return isDeleted ? ResponseEntity.ok("Pet deleted successfully") : ResponseEntity.status(403).body("Access denied");
    }

    private String getAuthenticatedUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}