package com.virtualpets.app.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Schema(description = "Represents a user in the virtual pet application")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Password of the user", example = "password123", writeOnly = true)
    private String password;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email of the user", example = "johndoe@example.com")
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Schema(description = "Roles assigned to the user", example = "[\"USER\", \"ADMIN\"]")
    private Set<String> roles;

    public User(){
    }

    public User(String username, String password, String email, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.email    = email;
        this.roles    = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}