package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long articleId,
                                        @Valid @RequestBody CommentRequest request) {
        try {
            Optional<Article> article = articleService.findById(articleId);
            if (!article.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Article non trouvé"));
            }

            // récupération de l’utilisateur via SecurityContextHolder
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));


            Comment comment = new Comment();
            comment.setContent(request.getContent().trim());
            comment.setAuthor(user);
            comment.setArticle(article.get());

            Comment saved = commentService.save(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de l'ajout du commentaire: " + e.getMessage()));
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        Optional<Comment> comment = commentService.findById(id);

        if (!comment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Commentaire non trouvé"));
        }

        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}