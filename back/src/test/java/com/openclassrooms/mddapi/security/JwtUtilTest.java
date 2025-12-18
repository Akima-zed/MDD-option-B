package com.openclassrooms.mddapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour JwtUtil - CRITIQUE pour la soutenance
 * Vérifie la génération et validation des tokens JWT
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    @DisplayName("Should generate valid JWT token for user ID")
    void testGenerateToken() {
        // Given
        Long userId = 123L;

        // When
        String token = jwtUtil.generateToken(userId);

        // Then
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
        assertTrue(token.split("\\.").length == 3, "JWT should have 3 parts (header.payload.signature)");
    }

    @Test
    @DisplayName("Should validate a valid JWT token")
    void testValidateToken_ValidToken() {
        // Given
        Long userId = 456L;
        String token = jwtUtil.generateToken(userId);

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    @DisplayName("Should reject invalid JWT token (malformed)")
    void testValidateToken_InvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Then
        assertFalse(isValid, "Malformed token should be invalid");
    }

    @Test
    @DisplayName("Should reject empty or null token")
    void testValidateToken_EmptyToken() {
        // When & Then
        assertFalse(jwtUtil.validateToken(""), "Empty token should be invalid");
        assertFalse(jwtUtil.validateToken(null), "Null token should be invalid");
    }

    @Test
    @DisplayName("Should extract correct user ID from valid token")
    void testExtractUserId() {
        // Given
        Long expectedUserId = 789L;
        String token = jwtUtil.generateToken(expectedUserId);

        // When
        Long extractedUserId = jwtUtil.extractUserId(token);

        // Then
        assertNotNull(extractedUserId, "Extracted user ID should not be null");
        assertEquals(expectedUserId, extractedUserId, "Extracted user ID should match original");
    }

    @Test
    @DisplayName("Should throw exception when extracting userId from invalid token")
    void testExtractUserId_InvalidToken() {
        // Given
        String invalidToken = "invalid.token.signature";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUserId(invalidToken);
        }, "Should throw exception for invalid token");
    }

    @Test
    @DisplayName("Should generate different tokens for different user IDs")
    void testGenerateToken_DifferentUserIds() {
        // Given
        Long userId1 = 100L;
        Long userId2 = 200L;

        // When
        String token1 = jwtUtil.generateToken(userId1);
        String token2 = jwtUtil.generateToken(userId2);

        // Then
        assertNotEquals(token1, token2, "Tokens for different users should be different");
    }

    @Test
    @DisplayName("Should generate different tokens for same user (different timestamps)")
    void testGenerateToken_SameUser_DifferentTimestamps() throws InterruptedException {
        // Given
        Long userId = 123L;

        // When
        String token1 = jwtUtil.generateToken(userId);
        Thread.sleep(1000); // Wait 1 second
        String token2 = jwtUtil.generateToken(userId);

        // Then
        assertNotEquals(token1, token2, "Tokens generated at different times should differ");
    }
}
