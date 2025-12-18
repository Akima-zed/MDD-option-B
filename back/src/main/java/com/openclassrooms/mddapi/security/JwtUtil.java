package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utilitaire pour la gestion des tokens JWT.
 * Permet de générer, valider et extraire des informations des tokens.
 */
@Component
public class JwtUtil {
    
    // Clé secrète pour signer les tokens (en production, utiliser une vraie clé sécurisée depuis application.properties)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Durée de validité du token : 24 heures
    private static final long EXPIRATION_TIME = 86400000; // 24h en millisecondes

    /**
     * Génère un token JWT pour un utilisateur.
     * 
     * @param userId L'ID de l'utilisateur
     * @return Le token JWT signé
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extrait l'ID utilisateur depuis un token JWT.
     * 
     * @param token Le token JWT
     * @return L'ID de l'utilisateur
     */
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Valide un token JWT.
     * 
     * @param token Le token JWT à valider
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
