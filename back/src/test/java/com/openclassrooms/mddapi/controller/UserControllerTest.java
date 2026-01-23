package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.dto.UserUpdateRequest;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    private void mockAuth(User user) {
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn(user.getId());
        when(userService.findById(user.getId())).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("GET /api/users/me - Doit retourner le profil de l'utilisateur connecté")
    void getCurrentUser_shouldReturnUser() throws Exception {
        Theme theme = new Theme();
        theme.setId(10L);
        theme.setNom("Java");
        theme.setDescription("Langage");

        User user = new User();
        user.setId(1L);
        user.setUsername("julie");
        user.setEmail("julie@test.com");
        user.setDateInscription(LocalDate.now());
        user.setAbonnements(Set.of(theme));

        mockAuth(user);

        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("julie"))
                .andExpect(jsonPath("$.abonnements[0].name").value("Java"));
    }

    @Test
    @DisplayName("PUT /api/users/me - Doit mettre à jour le profil de l'utilisateur")
    void updateUser_shouldWork() throws Exception {
        UserUpdateRequest updated = new UserUpdateRequest();
        updated.setUsername("julie2");
        updated.setEmail("new@test.com");

        User saved = new User();
        saved.setId(1L);
        saved.setUsername("julie2");
        saved.setEmail("new@test.com");
        saved.setDateInscription(LocalDate.now());
        saved.setAbonnements(new HashSet<>());

        mockAuth(saved);
        when(userService.update(eq(1L), any(UserUpdateRequest.class))).thenReturn(saved);

        mockMvc.perform(put("/api/users/me")
                .header("Authorization", "Bearer faketoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("julie2"));
    }

    @Test
    @DisplayName("Doit retourner 403 sans authentification (getCurrentUser)")
    void getCurrentUser_shouldReturn403_whenNotAuthenticated() throws Exception {
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer invalid"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Doit retourner 403 sans authentification (updateUser)")
    void updateUser_shouldReturn403_whenNotAuthenticated() throws Exception {
        UserUpdateRequest updated = new UserUpdateRequest();
        updated.setUsername("julie2");
        updated.setEmail("new@test.com");

        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(put("/api/users/me")
                .header("Authorization", "Bearer invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isForbidden());
    }
}