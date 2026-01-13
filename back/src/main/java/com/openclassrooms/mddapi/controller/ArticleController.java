package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleRequest;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ThemeService themeService;

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.findAllOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);

        if (!article.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Article non trouvé"));
        }

        return ResponseEntity.ok(article.get());
    }

    @PostMapping
    public ResponseEntity<?> createArticle(@Valid @RequestBody ArticleRequest request) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<Theme> theme = themeService.findById(request.getThemeId());
            if (!theme.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Thème non trouvé"));
            }

            Article article = new Article();
            article.setTitle(request.getTitle());
            article.setContent(request.getContent());
            article.setAuthor(user);
            article.setTheme(theme.get());

            Article saved = articleService.save(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de la création de l'article: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getArticleComments(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);

        if (!article.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Article non trouvé"));
        }

        return ResponseEntity.ok(article.get().getComments());
    }

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
}