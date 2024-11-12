package com.virtualpets.app.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of pet")
public enum PetType {
    @Schema(description = "A dragon pet")
    DRAGON,
    @Schema(description = "A unicorn pet")
    UNICORN,
    @Schema(description = "An alien pet")
    ALIEN,
    @Schema(description = "A cat pet")
    CAT,
    @Schema(description = "A dog pet")
    DOG
}