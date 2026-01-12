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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des commentaires.
 */
@RestController
@RequestMapping("/api/articles")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private UserService userService;

    /**
     * Ajoute un commentaire à un article.
     */
    @PostMapping("/{articleId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long articleId,
                                        @Valid @RequestBody CommentRequest request) {
        try {
            // Vérifier que l'article existe
            Optional<Article> article = articleService.findById(articleId);
            if (!article.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Article non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
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
            
            // Créer le commentaire
            Comment comment = new Comment();
            comment.setContent(request.getContent());
            comment.setAuthor(user.get());
            comment.setArticle(article.get());
            
            Comment savedComment = commentService.save(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de l'ajout du commentaire: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Supprime un commentaire.
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        Optional<Comment> comment = commentService.findById(id);
        
        if (!comment.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Commentaire non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
