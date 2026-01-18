package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


/**
 * Repository pour la gestion des utilisateurs.
 * Fournit des méthodes de recherche par email ou nom d'utilisateur.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
