package com.virtualpets.app.services;

import com.virtualpets.app.models.User;
import com.virtualpets.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Retorna un objecte UserDetails amb els detalls de l'usuari
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use.");
        }
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        user.setRoles(Collections.singleton("ROLE_USER")); // Assigna el rol d'usuari
        userRepository.save(user);
        logger.info("Usuari registrat amb èxit: {}", user.getUsername());
    }

    public void createAdminIfNotExists(String username, String password, String email) {
        if (userRepository.findByUsername(username).isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_ADMIN");

            User admin = new User();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password)); // Contrasenya codificada
            admin.setEmail(email);
            admin.setRoles(roles);

            userRepository.save(admin);
            logger.info("Usuari administrador creat amb èxit: {}", username);
        } else {
            logger.info("L'usuari administrador {} ja existeix. No es crearà un de nou.", username);
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Obtenir els rols d'un usuari per nom d'usuari
    public Set<String> getRoles(String username) {
        return findByUsername(username)
                .map(User::getRoles)
                .orElse(Collections.emptySet());
    }
}