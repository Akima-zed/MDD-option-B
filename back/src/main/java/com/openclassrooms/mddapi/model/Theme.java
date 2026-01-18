package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un thème auquel les utilisateurs peuvent s'abonner.
 */
@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "abonnements")
    @JsonIgnoreProperties({"abonnements", "articles", "comments", "password"})
    private Set<User> abonnes = new HashSet<>();

    @OneToMany(mappedBy = "theme")
    @JsonIgnoreProperties({"theme", "comments"})
    private Set<Article> articles = new HashSet<>();

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<User> getAbonnes() { return abonnes; }
    public void setAbonnes(Set<User> abonnes) { this.abonnes = abonnes; }

    public Set<Article> getArticles() { return articles; }
    public void setArticles(Set<Article> articles) { this.articles = articles; }
}