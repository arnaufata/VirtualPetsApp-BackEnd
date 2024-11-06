package com.virtualpets.app.models;

import jakarta.persistence.*;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType type;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private int energyLevel;    // Nivell d'energia (0-100)
    private int hungerLevel;    // Nivell de gana (0-100)
    private int happinessLevel; // Nivell de felicitat (0-100)

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