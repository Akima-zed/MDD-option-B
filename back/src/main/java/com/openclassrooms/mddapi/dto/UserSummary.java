package com.openclassrooms.mddapi.dto;

/**
 * Représente une version simplifiée d'un utilisateur.
 * Utilisé notamment pour afficher l'auteur d'un article ou d'un commentaire.
 */
public class UserSummary {
    private Long id;
    private String username;

    public UserSummary(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
}