package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.CommentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private CommentService commentService;

    @MockBean
    private ArticleService articleService;

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
    @DisplayName("POST /api/articles/{id}/comments - Doit renvoyer 403 si l'utilisateur n'est pas authentifié")
    void addComment_shouldReturn401_whenNotLogged() throws Exception {
        CommentRequest req = new CommentRequest("Hello");

        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/articles/10/comments")
                .header("Authorization", "Bearer faketoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/articles/{id}/comments - Doit créer un commentaire si l'utilisateur est authentifié")
    void addComment_shouldCreate() throws Exception {
        CommentRequest req = new CommentRequest("Hello");

        User user = new User();
        user.setId(1L);

        Article article = new Article();
        article.setId(10L);

        Comment saved = new Comment();
        saved.setId(100L);
        saved.setContent("Hello");
        saved.setAuthor(user);
        saved.setArticle(article);
        saved.setDateCreation(LocalDate.now());

        mockAuth(user);
        when(articleService.findById(10L)).thenReturn(Optional.of(article));
        when(commentService.save(any(Comment.class))).thenReturn(saved);

        mockMvc.perform(post("/api/articles/10/comments")
                .header("Authorization", "Bearer faketoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    @DisplayName("DELETE /api/articles/comments/{id} - Doit renvoyer 404 si le commentaire n'existe pas")
    void deleteComment_shouldReturn404() throws Exception {
        User user = new User();
        user.setId(1L);

        mockAuth(user);
        when(commentService.findById(100L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/articles/comments/100")
                .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/articles/comments/{id} - Doit supprimer le commentaire si l'utilisateur est authentifié")
    void deleteComment_shouldDelete() throws Exception {
        User user = new User();
        user.setId(1L);

        Comment c = new Comment();
        c.setId(100L);

        mockAuth(user);
        when(commentService.findById(100L)).thenReturn(Optional.of(c));

        mockMvc.perform(delete("/api/articles/comments/100")
                .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Doit retourner 404 si article n'existe pas")
    void addComment_shouldReturn404_whenArticleNotFound() throws Exception {
        CommentRequest req = new CommentRequest("Hello");

        User user = new User();
        user.setId(1L);

        mockAuth(user);
        when(articleService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/articles/999/comments")
                .header("Authorization", "Bearer faketoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/articles/{id}/comments - Doit créer un commentaire avec succès")
    void addComment_shouldCreateComment_whenSuccess() throws Exception {
        CommentRequest req = new CommentRequest("Great article!");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Article article = new Article();
        article.setId(1L);
        article.setTitle("Test Article");

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent("Great article!");
        savedComment.setAuthor(user);
        savedComment.setArticle(article);

        mockAuth(user);
        when(articleService.findById(1L)).thenReturn(Optional.of(article));
        when(commentService.save(any(Comment.class))).thenReturn(savedComment);

        mockMvc.perform(post("/api/articles/1/comments")
                .header("Authorization", "Bearer faketoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}