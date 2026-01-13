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
    @DisplayName("Should generate valid JWT token for user ID")
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
    @DisplayName("Should reject invalid JWT token (malformed)")
    void testValidateToken_InvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    @DisplayName("Should reject empty or null token")
    void testValidateToken_EmptyToken() {
        assertFalse(jwtUtil.validateToken(""));
        assertFalse(jwtUtil.validateToken(null));
    }

    @Test
    @DisplayName("Should extract correct user ID from valid token")
    void testExtractUserId() {
        Long expectedUserId = 789L;
        String token = jwtUtil.generateToken(expectedUserId);

        Long extractedUserId = jwtUtil.extractUserId(token);

        assertEquals(expectedUserId, extractedUserId);
    }

    @Test
    @DisplayName("Should throw exception when extracting userId from invalid token")
    void testExtractUserId_InvalidToken() {
        assertThrows(Exception.class, () -> jwtUtil.extractUserId("invalid.token.signature"));
    }

    @Test
    @DisplayName("Should generate different tokens for different user IDs")
    void testGenerateToken_DifferentUserIds() {
        String token1 = jwtUtil.generateToken(100L);
        String token2 = jwtUtil.generateToken(200L);

        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Should generate different tokens for same user (different timestamps)")
    void testGenerateToken_SameUser_DifferentTimestamps() throws InterruptedException {
        String token1 = jwtUtil.generateToken(123L);
        Thread.sleep(1000);
        String token2 = jwtUtil.generateToken(123L);

        assertNotEquals(token1, token2);
    }
}