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
import java.util.stream.Collectors;

import java.util.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
public ResponseEntity<?> getCurrentUserProfile() {
    Long userId = SecurityUtils.getCurrentUserId();

    if (userId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Utilisateur non authentifié"));
    }

    User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    // 🔥 Conversion des abonnements en ThemeResponse
    Set<ThemeResponse> abonnements = user.getAbonnements()
            .stream()
            .map(t -> new ThemeResponse(
                    t.getId(),
                    t.getNom(),
                    t.getDescription()
            ))
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

    /**
     * Récupère les abonnements (thèmes) d'un utilisateur par son id.
     * (peut être gardé pour debug / admin, pas utilisé par le front MDD)
     */
    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<?> getUserSubscriptions(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        Set<Theme> abonnements = user.get().getAbonnements();
        return ResponseEntity.ok(abonnements);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * Met à jour les informations d'un utilisateur (username et email uniquement).
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.update(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}