package com.virtualpets.app.repositories;

import com.virtualpets.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Mètode per trobar un usuari pel seu nom d'usuari
    Optional<User> findByUsername(String username);

    // Mètode per comprovar si un nom d'usuari ja existeix
    boolean existsByUsername(String username);
}

