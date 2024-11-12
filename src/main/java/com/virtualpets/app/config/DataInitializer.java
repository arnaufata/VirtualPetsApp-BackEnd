package com.virtualpets.app.config;

import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.PetType;
import com.virtualpets.app.models.User;
import com.virtualpets.app.repositories.PetRepository;
import com.virtualpets.app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(@Lazy UserRepository userRepository, PetRepository petRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.petRepository   = petRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            createAdminIfNotExists();
            createDefaultPetsIfNotExists();
        };
    }

    private void createAdminIfNotExists() {
        String username = "admin";
        if (userRepository.findByUsername(username).isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_ADMIN");

            User admin = new User();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode("adminpassword"));
            admin.setEmail("admin@domain.com");
            admin.setRoles(roles);

            userRepository.save(admin);
            logger.info("Usuari administrador creat amb èxit: {}", username);
        } else {
            logger.info("L'usuari administrador {} ja existeix. No es crearà un de nou.", username);
        }
    }

    private void createDefaultPetsIfNotExists() {
        if (petRepository.count() == 0) {
            User admin = userRepository.findByUsername("admin")
                    .orElseThrow(() -> new IllegalStateException("Admin user not found to assign default pets to."));

            Pet dragon = new Pet("DRAGON", PetType.DRAGON, "Red", admin);
            Pet unicorn = new Pet("UNICORN", PetType.UNICORN, "White", admin);
            Pet alien = new Pet("ALIEN", PetType.ALIEN, "Green", admin);
            Pet cat = new Pet("CAT", PetType.CAT, "Black", admin);

            petRepository.save(dragon);
            petRepository.save(unicorn);
            petRepository.save(alien);
            petRepository.save(cat);

            logger.info("Mascotes predeterminades creades amb èxit.");
        } else {
            logger.info("Ja existeixen mascotes a la base de dades. No es crearan mascotes predeterminades.");
        }
    }
}