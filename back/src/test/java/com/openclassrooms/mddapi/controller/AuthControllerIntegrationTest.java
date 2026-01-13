package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour AuthController
 * Teste l'authentification complète avec BCrypt et JWT
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("johndoe");
        testUser.setEmail("john@test.com");
        testUser.setPassword(passwordEncoder.encode("Password123!"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should register new user with BCrypt password")
    void testRegister_Success() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@test.com");
        request.setPassword("Password123!");

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@test.com");

        when(userService.existsByEmail(anyString())).thenReturn(false);
        when(userService.existsByUsername(anyString())).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(2L)).thenReturn("fake-jwt-token");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@test.com"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should reject duplicate email")
    void testRegister_DuplicateEmail() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("existing@test.com");
        request.setPassword("Password123!");

        when(userService.existsByEmail("existing@test.com")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cet email est déjà utilisé"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should reject duplicate username")
    void testRegister_DuplicateUsername() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setEmail("new@test.com");
        request.setPassword("Password123!");

        when(userService.existsByEmail(anyString())).thenReturn(false);
        when(userService.existsByUsername("existinguser")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ce nom d'utilisateur est déjà pris"));
    }

    @Test
    @DisplayName("POST /api/auth/login - Should login with valid credentials and BCrypt")
    void testLogin_Success() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("john@test.com");
        request.setPassword("Password123!");

        when(userService.findByEmail("john@test.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(1L)).thenReturn("fake-jwt-token");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    @DisplayName("POST /api/auth/login - Should reject invalid password")
    void testLogin_InvalidPassword() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("john@test.com");
        request.setPassword("wrongpassword");

        when(userService.findByEmail("john@test.com")).thenReturn(Optional.of(testUser));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Identifiants invalides"));
    }

    @Test
    @DisplayName("POST /api/auth/login - Should reject non-existent user")
    void testLogin_UserNotFound() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("notfound@test.com");
        request.setPassword("Password123!");

        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Identifiants invalides"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should reject weak/invalid password")
    void testRegister_WeakPassword() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("weakuser");
        request.setEmail("weak@test.com");
        request.setPassword("weak"); // too short and missing required classes

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - Should login with username instead of email")
    void testLogin_WithUsername() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("johndoe");
        request.setPassword("Password123!");

        when(userService.findByEmail("johndoe")).thenReturn(Optional.empty());
        when(userService.findByUsername("johndoe")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(1L)).thenReturn("fake-jwt-token");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }
}
