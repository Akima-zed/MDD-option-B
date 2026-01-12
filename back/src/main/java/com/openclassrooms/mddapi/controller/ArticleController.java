package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleRequest;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des articles.
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ThemeService themeService;

    /**
     * Récupère tous les articles triés par date décroissante.
     */
    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.findAllOrderByCreatedAtDesc();
    }

    /**
     * Récupère un article par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);
        
        if (!article.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Article non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(article.get());
    }

    /**
     * Crée un nouvel article.
     */
    @PostMapping
    public ResponseEntity<?> createArticle(@Valid @RequestBody ArticleRequest request) {
        try {
            // Récupérer l'utilisateur courant depuis SecurityContext
            Long userId = com.openclassrooms.mddapi.security.SecurityUtils.getCurrentUserId();

            if (userId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Utilisateur non authentifié");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            Optional<User> user = userService.findById(userId);
            if (!user.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Utilisateur non trouvé");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            Optional<Theme> theme = themeService.findById(request.getThemeId());
            if (!theme.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Thème non trouvé");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Article article = new Article();
            article.setTitle(request.getTitle());
            article.setContent(request.getContent());
            article.setAuthor(user.get());
            article.setTheme(theme.get());
            
            Article savedArticle = articleService.save(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la création de l'article: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Récupère tous les commentaires d'un article.
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getArticleComments(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);
        
        if (!article.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Article non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(article.get().getCommentaires());
    }

    /**
     * Supprime un article.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);
        
        if (!article.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Article non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
