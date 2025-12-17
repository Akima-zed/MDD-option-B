package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @JsonProperty("contenu")
    private String content;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"password", "articles", "commentaires", "abonnements"})
    @JsonProperty("auteur")
    private User author;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"commentaires"})
    private Article article;

    private LocalDate dateCreation = LocalDate.now();

    // Getters and setters
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
