package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion de l'authentification.
 * Expose les endpoints /api/auth/register et /api/auth/login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Inscription d'un nouvel utilisateur.
     * 
     * @param request DTO contenant username, email et password
     * @return AuthResponse avec token JWT et infos utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Vérifier si l'email existe déjà
            if (userService.existsByEmail(request.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Cet email est déjà utilisé");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Vérifier si le username existe déjà
            if (userService.existsByUsername(request.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Ce nom d'utilisateur est déjà pris");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Créer le nouvel utilisateur
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            // Hasher le mot de passe avec BCrypt
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            
            User savedUser = userService.save(user);

            // Générer un vrai token JWT signé
            String token = jwtUtil.generateToken(savedUser.getId());

            AuthResponse response = new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de l'inscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Connexion d'un utilisateur existant.
     * 
     * @param request DTO contenant emailOrUsername et password
     * @return AuthResponse avec token JWT et infos utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Chercher l'utilisateur par email ou username
            User user = userService.findByEmail(request.getEmailOrUsername())
                .orElse(userService.findByUsername(request.getEmailOrUsername())
                    .orElse(null));

            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Identifiants invalides");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Vérifier le mot de passe avec BCrypt
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Identifiants invalides");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Générer un vrai token JWT signé
            String token = jwtUtil.generateToken(user.getId());

            AuthResponse response = new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail()
            );

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la connexion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
