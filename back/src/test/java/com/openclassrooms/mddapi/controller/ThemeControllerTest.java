package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ThemeService themeService;

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
    @DisplayName("Doit retourner la liste des thèmes")
    void getAllThemes_shouldReturnList() throws Exception {
        Theme t = new Theme();
        t.setId(10L);
        t.setNom("Java");
        t.setDescription("Langage");

        when(themeService.findAll()).thenReturn(List.of(t));

        mockMvc.perform(get("/api/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));
    }

    @Test
    @DisplayName("Doit abonner l'utilisateur au thème")
    void subscribe_shouldWork() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setAbonnements(new HashSet<>());

        Theme theme = new Theme();
        theme.setId(10L);

        mockAuth(user);
        when(themeService.findById(10L)).thenReturn(Optional.of(theme));

        mockMvc.perform(post("/api/themes/10/subscribe")
                .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Abonnement réussi"));
    }

    @Test
    @DisplayName("Doit désabonner l'utilisateur du thème")
    void unsubscribe_shouldWork() throws Exception {
        Theme theme = new Theme();
        theme.setId(10L);

        User user = new User();
        user.setId(1L);
        user.setAbonnements(new HashSet<>(Set.of(theme)));

        mockAuth(user);
        when(themeService.findById(10L)).thenReturn(Optional.of(theme));
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/themes/10/unsubscribe")
                .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Désabonnement réussi"));
    }

    @Test
    @DisplayName("Doit retourner 403 sans authentification (subscribe)")
    void subscribe_shouldReturn403_whenNotAuthenticated() throws Exception {
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/themes/10/subscribe")
                .header("Authorization", "Bearer invalid"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Doit retourner 403 sans authentification (unsubscribe)")
    void unsubscribe_shouldReturn403_whenNotAuthenticated() throws Exception {
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/themes/10/unsubscribe")
                .header("Authorization", "Bearer invalid"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Doit retourner une liste vide")
    void getAllThemes_shouldReturnEmptyList() throws Exception {
        when(themeService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}