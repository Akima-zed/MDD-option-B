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

    // -------------------------------------------------------
    // GET ALL THEMES — DTO + état d’abonnement
    // -------------------------------------------------------
    @GetMapping
    public List<ThemeResponse> getAllThemes() {

        Long userId = SecurityUtils.getCurrentUserId();
        User currentUser = null;

        if (userId != null) {
            currentUser = userService.findById(userId).orElse(null);
        }

        User finalUser = currentUser;

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

    // -------------------------------------------------------
    // GET THEME BY ID — DTO only
    // -------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getThemeById(@PathVariable Long id) {

        Long userId = SecurityUtils.getCurrentUserId();
        User currentUser = null;

        if (userId != null) {
            currentUser = userService.findById(userId).orElse(null);
        }

        Theme theme = themeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

        boolean subscribed = currentUser != null &&
                currentUser.getAbonnements().stream()
                        .anyMatch(a -> a.getId().equals(theme.getId()));

        ThemeResponse response = new ThemeResponse(
                theme.getId(),
                theme.getNom(),
                theme.getDescription(),
                subscribed
        );

        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------
    // SUBSCRIBE
    // -------------------------------------------------------
    @PostMapping("/{themeId}/subscribe")
    public ResponseEntity<?> subscribeToTheme(@PathVariable Long themeId) {

        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur non authentifié"));
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Theme theme = themeService.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

        boolean alreadySubscribed = user.getAbonnements().stream()
                .anyMatch(t -> t.getId().equals(theme.getId()));

        if (alreadySubscribed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Déjà abonné à ce thème"));
        }

        user.getAbonnements().add(theme);
        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "Abonnement réussi"));
    }

    // -------------------------------------------------------
    // UNSUBSCRIBE
    // -------------------------------------------------------
    @PostMapping("/{themeId}/unsubscribe")
    public ResponseEntity<?> unsubscribeFromTheme(@PathVariable Long themeId) {

        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur non authentifié"));
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Theme theme = themeService.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

        boolean wasSubscribed = user.getAbonnements().removeIf(t -> t.getId().equals(theme.getId()));

        if (!wasSubscribed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Vous n'étiez pas abonné à ce thème"));
        }

        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "Désabonnement réussi"));
    }
}