package com.virtualpets.app.controllers;

import com.virtualpets.app.models.User;
import com.virtualpets.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUserDetails(Authentication authentication) {
        Optional<User> user = userService.findByUsername(authentication.getName());
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
