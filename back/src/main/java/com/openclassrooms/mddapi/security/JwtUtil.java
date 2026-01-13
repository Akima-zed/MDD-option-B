package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Service utilitaire pour la gestion des tokens JWT.
 * Permet de générer, valider et extraire des informations depuis un token.
 * 
 * Le token contient :
 * - subject = ID utilisateur
 * - date d'émission
 * - date d'expiration
 * 
 * Le token est signé avec une clé secrète HS256.
 */
@Component
public class JwtUtil {

    /** 
     * Clé secrète utilisée pour signer les tokens.
     * En production : stocker dans application.properties ou une variable d'environnement.
     */
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /** Durée de validité du token : 24 heures */
    private static final long EXPIRATION_TIME = 86400000; // 24h

    /**
     * Génère un token JWT contenant l'ID utilisateur dans le "subject".
     *
     * @param userId ID de l'utilisateur
     * @return Token JWT signé
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // ID utilisateur
                .setIssuedAt(now)                     // Date d'émission
                .setExpiration(expiryDate)            // Date d'expiration
                .signWith(SECRET_KEY)                 // Signature
                .compact();
    }

    /**
     * Extrait l'ID utilisateur depuis un token JWT.
     *
     * @param token Token JWT brut (sans "Bearer ")
     * @return ID utilisateur
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
     * Vérifie si un token JWT est valide (signature + expiration).
     *
     * @param token Token JWT
     * @return true si valide, false sinon
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