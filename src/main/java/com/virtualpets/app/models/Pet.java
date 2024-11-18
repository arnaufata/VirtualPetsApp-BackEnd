package com.virtualpets.app.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Represents a virtual pet owned by a user")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the pet", example = "1")
    private long id;

    @Column(nullable = false)
    @Schema(description = "Name of the pet", example = "Buddy")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Type of the pet", example = "DOG")
    private PetType type;

    @Column(nullable = false)
    @Schema(description = "Color of the pet", example = "brown")
    private String color;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @Schema(description = "Owner of the pet, represented by the User entity")
    private User owner;

    @Schema(description = "Energy level of the pet, ranges from 0 to 100", example = "80")
    private int energyLevel;

    @Schema(description = "Hunger level of the pet, ranges from 0 to 100", example = "20")
    private int hungerLevel;

    @Schema(description = "Happiness level of the pet, ranges from 0 to 100", example = "90")
    private int happinessLevel;

    public Pet() {
    }

    public Pet(String name, PetType type, String color, User owner) {
        this.name           = name;
        this.type           = type;
        this.color          = color;
        this.owner          = owner;
        this.energyLevel    = 100;    // Inicialment màxim
        this.hungerLevel    = 0;      // Inicialment sense gana
        this.happinessLevel = 100; // Inicialment feliç
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    // Mètodes per interactuar amb la mascota
    public void feed() {
        this.hungerLevel = Math.max(0, this.hungerLevel - 20);         // Redueix la gana
        this.happinessLevel = Math.min(100, this.happinessLevel + 10); // Augmenta la felicitat
    }

    public void play() {
        this.energyLevel = Math.max(0, this.energyLevel - 20);         // Redueix energia
        this.hungerLevel = Math.min(100, this.hungerLevel + 10);       // Augmenta la gana
        this.happinessLevel = Math.min(100, this.happinessLevel + 20); // Augmenta la felicitat
    }

    public void rest() {
        this.energyLevel = Math.min(100, this.energyLevel + 30);     // Augmenta energia
        this.happinessLevel = Math.max(0, this.happinessLevel - 10); // Redueix felicitat (potser)
    }
}