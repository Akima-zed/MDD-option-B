package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"password", "articles", "commentaires", "abonnements"})
    private User author;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"articles", "abonnes"})
    private Theme theme;

    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties({"article"})
    private Set<Comment> commentaires = new HashSet<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Theme getTheme() { return theme; }
    public void setTheme(Theme theme) { this.theme = theme; }
    public Set<Comment> getCommentaires() { return commentaires; }
    public void setCommentaires(Set<Comment> commentaires) { this.commentaires = commentaires; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
