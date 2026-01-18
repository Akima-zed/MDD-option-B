package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Contrôleur pour gérer les articles :
 * - affichage
 * - consultation
 * - création
 * - suppression
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired 
    private ThemeService themeService;

    @Autowired
    private UserService userService;


    
    // Retourne la liste des articles (triés par date)
    @GetMapping
    public List<ArticleResponse> getAllArticles() {
        return articleService.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToArticleResponse)
                .collect(Collectors.toList());
    }

     // Retourne un article selon son ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);

        if (!article.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Article non trouvé"));
        }

        return ResponseEntity.ok(mapToArticleResponse(article.get()));
    }

    // Création d’un nouvel article
    @PostMapping
     public ResponseEntity<?> createArticle(@Valid @RequestBody ArticleRequest request) {
         try {
                // Récupération de l’utilisateur connecté via le token
                Long userId = SecurityUtils.getCurrentUserId();
                if (userId == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Utilisateur non authentifié"));
                }

                User user = userService.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

                Theme theme = themeService.findById(request.getThemeId())
                        .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

                // construction de l’article
                Article article = new Article();
                article.setTitle(request.getTitle());
                article.setContent(request.getContent());
                article.setAuthor(user);
                article.setTheme(theme);

                Article saved = articleService.save(article);

                return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToArticleResponse(saved));

        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Erreur lors de la création de l'article: " + e.getMessage()));
        }
}


    // Retourne les commentaires d’un article
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getArticleComments(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);

        if (!article.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Article non trouvé"));
        }

        List<CommentResponse> comments = article.get().getComments()
                .stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(comments);
    }

    // suppression d’un article par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);

        if (!article.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Article non trouvé"));
        }

        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // conversion Article -> ArticleResponse
    private ArticleResponse mapToArticleResponse(Article article) {
        Theme theme = article.getTheme();
        User author = article.getAuthor();

        ThemeResponse themeDto = new ThemeResponse(
                theme.getId(),
                theme.getNom(),
                theme.getDescription(),
                false
        );

        UserSummary authorDto = new UserSummary(
                author.getId(),
                author.getUsername()
        );

        List<CommentResponse> comments = article.getComments()
                .stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());

        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                themeDto,
                authorDto,
                comments
        );
    }

        // conversion Comment -> CommentResponse
    private CommentResponse mapToCommentResponse(Comment comment) {
        UserSummary authorDto = new UserSummary(
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername()
        );

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                authorDto,
                comment.getDateCreation()
        );
    }
}