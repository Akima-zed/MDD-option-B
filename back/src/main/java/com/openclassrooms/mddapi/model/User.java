package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    private LocalDate dateInscription = LocalDate.now();

    @ManyToMany
    @JoinTable(
        name = "user_theme",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    @JsonIgnoreProperties({"abonnes", "articles"})
    private Set<Theme> abonnements = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @JsonIgnoreProperties({"author", "commentaires"})
    private Set<Article> articles = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @JsonIgnoreProperties({"author", "article"})
    private Set<Comment> commentaires = new HashSet<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }
    public Set<Theme> getAbonnements() { return abonnements; }
    public void setAbonnements(Set<Theme> abonnements) { this.abonnements = abonnements; }
    public Set<Article> getArticles() { return articles; }
    public void setArticles(Set<Article> articles) { this.articles = articles; }
    public Set<Comment> getCommentaires() { return commentaires; }
    public void setCommentaires(Set<Comment> commentaires) { this.commentaires = commentaires; }
}
