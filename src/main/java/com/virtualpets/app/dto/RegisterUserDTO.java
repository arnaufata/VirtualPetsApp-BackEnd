package com.virtualpets.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for user registration")
public class RegisterUserDTO {

    @Schema(description = "Username of the new user", example = "john_doe")
    private String username;

    @Schema(description = "Password of the new user", example = "password123", writeOnly = true)
    private String password;

    @Schema(description = "Email of the new user", example = "john.doe@example.com")
    private String email;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email    = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
