package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des thèmes.
 */
@RestController
@RequestMapping("/api/themes")
public class ThemeController {
    
    @Autowired
    private ThemeService themeService;
    
    @Autowired
    private UserService userService;

    /**
     * Récupère tous les thèmes.
     */
    @GetMapping
    public List<Theme> getAllThemes() {
        return themeService.findAll();
    }

    /**
     * Récupère un thème par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getThemeById(@PathVariable Long id) {
        Optional<Theme> theme = themeService.findById(id);
        
        if (!theme.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Thème non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(theme.get());
    }

    /**
     * S'abonner à un thème.
     */
    @PostMapping("/{themeId}/subscribe")
    public ResponseEntity<?> subscribeToTheme(@PathVariable Long themeId) {
        try {
            // Récupérer l'utilisateur courant depuis SecurityContext
            Long userId = com.openclassrooms.mddapi.security.SecurityUtils.getCurrentUserId();
            if (userId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Utilisateur non authentifié");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            Optional<User> userOpt = userService.findById(userId);
            
            if (!userOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Utilisateur non trouvé");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            Optional<Theme> themeOpt = themeService.findById(themeId);
            if (!themeOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Thème non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            User user = userOpt.get();
            Theme theme = themeOpt.get();
            
            // Ajouter le thème aux abonnements de l'utilisateur
            user.getAbonnements().add(theme);
            userService.save(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Abonnement réussi");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de l'abonnement: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Se désabonner d'un thème.
     */
    @DeleteMapping("/{themeId}/subscribe")
    public ResponseEntity<?> unsubscribeFromTheme(@PathVariable Long themeId) {
        try {
            Long userId = com.openclassrooms.mddapi.security.SecurityUtils.getCurrentUserId();
            if (userId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Utilisateur non authentifié");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            Optional<User> userOpt = userService.findById(userId);
            
            if (!userOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Utilisateur non trouvé");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            Optional<Theme> themeOpt = themeService.findById(themeId);
            if (!themeOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Thème non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            User user = userOpt.get();
            Theme theme = themeOpt.get();
            
            // Retirer le thème des abonnements
            user.getAbonnements().remove(theme);
            userService.save(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Désabonnement réussi");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors du désabonnement: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Crée un nouveau thème (admin uniquement - à sécuriser).
     */
    @PostMapping
    public ResponseEntity<?> createTheme(@RequestBody Theme theme) {
        try {
            Theme savedTheme = themeService.save(theme);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTheme);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la création du thème: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Supprime un thème.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheme(@PathVariable Long id) {
        Optional<Theme> theme = themeService.findById(id);
        
        if (!theme.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Thème non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
