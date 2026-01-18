package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entité représentant un commentaire associé à un article.
 */
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @JsonProperty("content")
    private String content;

    @ManyToOne(optional = false)
    @JsonProperty("author")
    @JsonIgnoreProperties({"password", "articles", "comments", "subscriptions"})
    private User author;

    @ManyToOne(optional = false)
    @JsonIgnore // 🔥 Empêche la boucle infinie Article → Comments → Article
    private Article article;

    @JsonProperty("dateCreation")
    private LocalDate dateCreation = LocalDate.now();

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
}