package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("title")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @JsonProperty("content")
    private String content;

    @ManyToOne(optional = false)
    @JsonProperty("author")
    @JsonIgnoreProperties({"password", "articles", "comments", "subscriptions"})
    private User author;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"articles", "subscribers"})
    private Theme theme;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("comments")
    @JsonIgnoreProperties({"article"}) // 🔥 Empêche la boucle inverse
    private Set<Comment> comments = new HashSet<>();

    @JsonProperty("dateCreation")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters / Setters
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

    public Set<Comment> getComments() { return comments; }
    public void setComments(Set<Comment> comments) { this.comments = comments; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}