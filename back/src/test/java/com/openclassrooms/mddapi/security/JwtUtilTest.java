package com.openclassrooms.mddapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openclassrooms.mddapi.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // Injection manuelle des valeurs nécessaires
        // (car @Value ne fonctionne pas dans un test unitaire pur)
        TestUtils.setField(jwtUtil, "secret", "testsecret123456789testsecret123456789");
        TestUtils.setField(jwtUtil, "expiration", 3600000L);
    }

    @Test
    @DisplayName("Doit générer un token JWT valide pour un identifiant utilisateur")
    void testGenerateToken() {
        Long userId = 123L;
        String token = jwtUtil.generateToken(userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    @DisplayName("Should validate a valid JWT token")
    void testValidateToken_ValidToken() {
        Long userId = 456L;
        String token = jwtUtil.generateToken(userId);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Doit rejeter un token JWT invalide (malformé)")
    void testValidateToken_InvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    @DisplayName("Doit rejeter un token vide ou null")
    void testValidateToken_EmptyToken() {
        assertFalse(jwtUtil.validateToken(""));
        assertFalse(jwtUtil.validateToken(null));
    }

    @Test
    @DisplayName("Doit extraire correctement l'ID utilisateur d'un token valide")
    void testExtractUserId() {
        Long expectedUserId = 789L;
        String token = jwtUtil.generateToken(expectedUserId);

        Long extractedUserId = jwtUtil.extractUserId(token);

        assertEquals(expectedUserId, extractedUserId);
    }

    @Test
    @DisplayName("Doit lever une exception lors de l'extraction de l'identifiant depuis un token invalide")
    void testExtractUserId_InvalidToken() {
        assertThrows(Exception.class, () -> jwtUtil.extractUserId("invalid.token.signature"));
    }

    @Test
    @DisplayName("Doit générer différents tokens pour différents identifiants utilisateur")
    void testGenerateToken_DifferentUserIds() {
        String token1 = jwtUtil.generateToken(100L);
        String token2 = jwtUtil.generateToken(200L);

        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Doit générer des tokens différents pour un même utilisateur à des instants différents")
    void testGenerateToken_SameUser_DifferentTimestamps() throws InterruptedException {
        String token1 = jwtUtil.generateToken(123L);
        Thread.sleep(1000);
        String token2 = jwtUtil.generateToken(123L);

        assertNotEquals(token1, token2);
    }
}