package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ArticleRequest;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private UserService userService;

    @MockBean
    private ThemeService themeService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("POST /api/articles - Should create article when authenticated")
    void testCreateArticle_Success() throws Exception {

        ArticleRequest request = new ArticleRequest();
        request.setTitle("New Article");
        request.setContent("Content");
        request.setThemeId(1L);

        User user = new User();
        user.setId(2L);
        user.setUsername("author");

        Theme theme = new Theme();
        theme.setId(1L);
        theme.setNom("Java");

        Article saved = new Article();
        saved.setId(10L);
        saved.setTitle("New Article");
        saved.setAuthor(user);
        saved.setTheme(theme);

        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn(2L);
        when(userService.findById(2L)).thenReturn(Optional.of(user));
        when(themeService.findById(1L)).thenReturn(Optional.of(theme));
        when(articleService.save(any(Article.class))).thenReturn(saved);

        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer faketoken")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @DisplayName("POST /api/articles - Should return 403 when user not found")
    void testCreateArticle_UserNotFound() throws Exception {

        ArticleRequest request = new ArticleRequest();
        request.setTitle("New Article");
        request.setContent("Content");
        request.setThemeId(1L);

        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn(99L);
        when(userService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer faketoken")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}