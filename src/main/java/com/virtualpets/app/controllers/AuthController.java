package com.virtualpets.app.controllers;

import com.virtualpets.app.models.User;
import com.virtualpets.app.security.JwtTokenProvider;
import com.virtualpets.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService           = userService;
        this.jwtTokenProvider      = jwtTokenProvider;
    }

    @Operation(summary = "Register a new user", description = "Register a new user in the system.")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Error in registration process", content = @Content)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody(description = "User to register") User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "User login", description = "Authenticate user and return a JWT token.")
    @ApiResponse(responseCode = "200", description = "Login successful, token generated", content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "403", description = "Invalid user credentials", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(description = "User credentials for login") User loginUser) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();


            String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities().toString());


            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid user");
        }
    }
}