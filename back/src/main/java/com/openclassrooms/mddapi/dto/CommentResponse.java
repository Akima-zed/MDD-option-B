package com.openclassrooms.mddapi.dto;

import java.time.LocalDate;

/**
 * Représente les informations d'un commentaire renvoyées au client.
 */
public class CommentResponse {
    private Long id;
    private String content;
    private UserSummary author;
    private LocalDate dateCreation;

    public CommentResponse(Long id, String content, UserSummary author, LocalDate dateCreation) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.dateCreation = dateCreation;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public UserSummary getAuthor() { return author; }
    public LocalDate getDateCreation() { return dateCreation; }
}