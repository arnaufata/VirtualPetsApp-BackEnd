package com.virtualpets.app.config;

import com.virtualpets.app.services.PetService;
import com.virtualpets.app.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final UserService userService;
    private final PetService petService;

    public DataInitializer(UserService userService, PetService petService){
        this.userService = userService;
        this.petService  = petService;
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // Creació d'usuari administrador
            userService.createAdminIfNotExists("admin", "adminpassword", "admin@domain.com");

            // Creació de mascotes predeterminades
            petService.createDefaultPetsIfNotExists();
        };
    }
}
