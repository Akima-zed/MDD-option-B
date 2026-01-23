package com.openclassrooms.mddapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour SecurityConfig
 */
@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    @DisplayName("SecurityFilterChain bean doit être créé")
    void testSecurityFilterChainBean() {
        assertNotNull(securityFilterChain);
    }

    @Test
    @DisplayName("SecurityFilterChain doit avoir des filtres configurés")
    void testSecurityFilterChainHasFilters() {
        assertNotNull(securityFilterChain.getFilters());
        assertFalse(securityFilterChain.getFilters().isEmpty());
    }
}
