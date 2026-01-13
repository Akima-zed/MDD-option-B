package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime dateCreation;
    private ThemeResponse theme;
    private UserSummary author;
    private List<CommentResponse> comments;

    public ArticleResponse(Long id,
                           String title,
                           String content,
                           LocalDateTime dateCreation,
                           ThemeResponse theme,
                           UserSummary author,
                           List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreation = dateCreation;
        this.theme = theme;
        this.author = author;
        this.comments = comments;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public ThemeResponse getTheme() { return theme; }
    public UserSummary getAuthor() { return author; }
    public List<CommentResponse> getComments() { return comments; }
}