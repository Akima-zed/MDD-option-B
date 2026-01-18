package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ThemeResponse;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.SecurityUtils;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private UserService userService;

   // Récupère tous les thèmes avec un indicateur indiquant si l'utilisateur est abonné
    @GetMapping
    public List<ThemeResponse> getAllThemes() {
        // Récupération de l'utilisateur connecté
        Long userId = SecurityUtils.getCurrentUserId();
        User currentUser = null;

        if (userId != null) {
        // Recherche de l'utilisateur en base
            currentUser = userService.findById(userId).orElse(null);
        }

        User finalUser = currentUser;

        // Transformation des thèmes en DTO avec info d'abonnement
        return themeService.findAll().stream()
                .map(t -> new ThemeResponse(
                        t.getId(),
                        t.getNom(),
                        t.getDescription(),
                        finalUser != null && finalUser.getAbonnements().stream()
                                .anyMatch(a -> a.getId().equals(t.getId()))
                ))
                .collect(Collectors.toList());
    }

    // Récupère un thème par son ID et renvoie un DTO
    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> getThemeById(@PathVariable Long id) {

        // Récupération de l'utilisateur connecté
        Long userId = SecurityUtils.getCurrentUserId();
        User currentUser = null;

        if (userId != null) {
            currentUser = userService.findById(userId).orElse(null);
        }

        // Recherche du thèm
        Theme theme = themeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

        // Vérifie si l'utilisateur est abonné à ce thème
        boolean subscribed = currentUser != null &&
                currentUser.getAbonnements().stream()
                        .anyMatch(a -> a.getId().equals(theme.getId()));

        // Construction du DTO
        ThemeResponse response = new ThemeResponse(
                theme.getId(),
                theme.getNom(),
                theme.getDescription(),
                subscribed
        );

        return ResponseEntity.ok(response);
    }

    // Abonne l'utilisateur connecté à un thème
    @PostMapping("/{themeId}/subscribe")
    public ResponseEntity<Map<String, String>> subscribeToTheme(@PathVariable Long themeId) {

        // Vérifie que l'utilisateur est connecté
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur non authentifié"));
        }

        // Récupération de l'utilisateur
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));


        // Récupération du thème
        Theme theme = themeService.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

        // Vérifie si déjà abonné
        boolean alreadySubscribed = user.getAbonnements().stream()
                .anyMatch(t -> t.getId().equals(theme.getId()));

        if (alreadySubscribed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Déjà abonné à ce thème"));
        }

        // Ajout de l'abonnement
        user.getAbonnements().add(theme);
        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "Abonnement réussi"));
    }

    // Désabonne l'utilisateur connecté d'un thème
    @PostMapping("/{themeId}/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribeFromTheme(@PathVariable Long themeId) {

        // Vérifie que l'utilisateur est connecté
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur non authentifié"));
        }

        // Récupération de l'utilisateur
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Récupération du thème
        Theme theme = themeService.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé"));


        // Suppression de l'abonnement
        boolean wasSubscribed = user.getAbonnements().removeIf(t -> t.getId().equals(theme.getId()));

        if (!wasSubscribed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Vous n'étiez pas abonné à ce thème"));
        }

        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "Désabonnement réussi"));
    }
}