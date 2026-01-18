package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository pour la gestion des commentaires.
 * Hérite des opérations CRUD standard.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
