package com.virtualpets.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for user login")
public class LoginUserDTO {

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Password of the user", example = "password123", writeOnly = true)
    private String password;

    public LoginUserDTO() {
    }

    public LoginUserDTO(String username, String password) {
        this.username = username;
        this.password = password;
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
}