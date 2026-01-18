package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service chargé de gérer les opérations liées aux articles.
 * Fournit des méthodes simples pour récupérer, enregistrer et supprimer des articles.
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

/**
     * Service gérant les opérations liées aux articles.
     * Les méthodes exposent les actions CRUD nécessaires au projet.
     */
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findAllOrderByCreatedAtDesc() {
        return articleRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }


}
