package com.virtualpets.app.dto;

import com.virtualpets.app.models.PetType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for creating a new pet")
public class PetCreateDTO {

    @Schema(description = "Name of the pet", example = "Buddy", required = true)
    private String name;

    @Schema(description = "Type of the pet", example = "DOG", required = true)
    private PetType type;

    @Schema(description = "Color of the pet", example = "brown", required = true)
    private String color;

    public PetCreateDTO() {
    }

    public PetCreateDTO(String name, PetType type, String color) {
        this.name = name;
        this.type = type;
        this.color = color;
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
}
