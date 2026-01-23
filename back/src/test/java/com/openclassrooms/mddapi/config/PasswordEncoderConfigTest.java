package com.openclassrooms.mddapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour PasswordEncoderConfig
 */
@SpringBootTest
@ActiveProfiles("test")
class PasswordEncoderConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("PasswordEncoder bean doit être créé")
    void testPasswordEncoderBean() {
        assertNotNull(passwordEncoder);
    }

    @Test
    @DisplayName("PasswordEncoder doit encoder correctement un mot de passe")
    void testPasswordEncode() {
        String rawPassword = "Password123!";
        String encoded = passwordEncoder.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoded.startsWith("$2a$") || encoded.startsWith("$2b$"));
    }

    @Test
    @DisplayName("PasswordEncoder doit valider un mot de passe correct")
    void testPasswordMatches() {
        String rawPassword = "Password123!";
        String encoded = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }

    @Test
    @DisplayName("PasswordEncoder doit rejeter un mot de passe incorrect")
    void testPasswordDoesNotMatch() {
        String rawPassword = "Password123!";
        String wrongPassword = "WrongPassword!";
        String encoded = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.matches(wrongPassword, encoded));
    }

    @Test
    @DisplayName("PasswordEncoder doit générer des hashes différents pour le même mot de passe")
    void testPasswordEncodeTwice() {
        String rawPassword = "Password123!";
        String encoded1 = passwordEncoder.encode(rawPassword);
        String encoded2 = passwordEncoder.encode(rawPassword);

        assertNotEquals(encoded1, encoded2);
        assertTrue(passwordEncoder.matches(rawPassword, encoded1));
        assertTrue(passwordEncoder.matches(rawPassword, encoded2));
    }
}
