package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ThemeResponse;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.SecurityUtils;
import com.openclassrooms.mddapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ❌ Interdit dans MDD : on supprime GET /users (exposition brute)
    // ❌ Interdit : GET /users/{id} (exposition brute)

    // ✔ Route officielle MDD : récupérer le profil utilisateur
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile() {
        Long userId = SecurityUtils.getCurrentUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur non authentifié"));
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Set<ThemeResponse> abonnements = user.getAbonnements()
                .stream()
                .map(t -> new ThemeResponse(t.getId(), t.getNom(), t.getDescription(), true))
                .collect(Collectors.toSet());

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDateInscription(),
                abonnements
        );

        return ResponseEntity.ok(response);
    }

    // ✔ Mise à jour du profil
    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody User updated) {
        Long userId = SecurityUtils.getCurrentUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur non authentifié"));
        }

        User saved = userService.update(userId, updated);

        UserResponse response = new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getDateInscription(),
                saved.getAbonnements().stream()
                        .map(t -> new ThemeResponse(t.getId(), t.getNom(), t.getDescription(), true))
                        .collect(Collectors.toSet())
        );

        return ResponseEntity.ok(response);
    }
}