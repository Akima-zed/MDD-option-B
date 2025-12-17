package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    /**
     * Récupère tous les articles triés par date de création décroissante.
     */
    List<Article> findAllByOrderByCreatedAtDesc();
}
