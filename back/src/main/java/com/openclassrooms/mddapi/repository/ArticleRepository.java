package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository permettant d'accéder aux données des articles.
 * Fournit des opérations CRUD et une méthode de tri par date de création.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
     //Retourne tous les articles triés par date de création décroissante.
    List<Article> findAllByOrderByCreatedAtDesc();
}
