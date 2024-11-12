package com.virtualpets.app.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Custom exception for when a user is not found")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}