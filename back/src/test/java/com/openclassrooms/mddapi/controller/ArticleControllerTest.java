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
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("Doit créer un article lorsque l'utilisateur est authentifié")
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
    @DisplayName("Doit retourner 403 lorsque l'utilisateur n'est pas trouvé")
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

    @Test
    @DisplayName("Doit retourner 404 lorsque article n'existe pas")
    void testGetArticleById_NotFound() throws Exception {

        when(articleService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/articles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Doit retourner l'article si existe")
    void testGetArticleById_Success() throws Exception {

        User user = new User();
        user.setId(2L);

        Theme theme = new Theme();
        theme.setId(1L);
        theme.setNom("Java");

        Article article = new Article();
        article.setId(10L);
        article.setTitle("Test Article");
        article.setContent("Test Content");
        article.setAuthor(user);
        article.setTheme(theme);

        when(articleService.findById(10L)).thenReturn(Optional.of(article));

        mockMvc.perform(get("/api/articles/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @DisplayName("Doit retourner la liste des articles")
    void testGetAllArticles() throws Exception {

        User user = new User();
        user.setId(2L);

        Theme theme = new Theme();
        theme.setId(1L);

        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Article 1");
        article1.setAuthor(user);
        article1.setTheme(theme);

        when(articleService.findAllOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(article1));

        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Doit retourner 403 sans authentification")
    void testDeleteArticle_Unauthorized() throws Exception {

        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(delete("/api/articles/1")
                .header("Authorization", "Bearer invalid"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Doit retourner une liste vide")
    void testGetAllArticles_Empty() throws Exception {

        when(articleService.findAllOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Doit retourner les commentaires d'un article")
    void testGetArticleComments_Success() throws Exception {

        User user = new User();
        user.setId(2L);
        user.setUsername("author");

        Theme theme = new Theme();
        theme.setId(1L);
        theme.setNom("Java");

        Article article = new Article();
        article.setId(10L);
        article.setTitle("Test Article");
        article.setContent("Test Content");
        article.setAuthor(user);
        article.setTheme(theme);
        article.setComments(new HashSet<>());

        when(articleService.findById(10L)).thenReturn(Optional.of(article));

        mockMvc.perform(get("/api/articles/10/comments"))
                .andExpect(status().isOk());
    }
}