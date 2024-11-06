package com.virtualpets.app.repositories;

import com.virtualpets.app.models.Pet;
import com.virtualpets.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Mètode per trobar totes les mascotes d'un usuari en concret
    List<Pet> findByOwner(User owner);

    // Mètode per trobar mascotes pel seu tipus
    List<Pet> findByType(String type);

    // Mètode per trobar totes les mascotes segons el seu nivell de felicitat (p. ex. per veure quines necessiten atenció)
    List<Pet> findByHappinessLevelLessThan(int happinessLevel);
}
