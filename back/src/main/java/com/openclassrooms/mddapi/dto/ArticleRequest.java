package com.openclassrooms.mddapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO pour la création d'un nouvel article.
 */
public class ArticleRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    @NotBlank(message = "Le contenu est obligatoire")
    private String content;
    
    @NotNull(message = "Le thème est obligatoire")
    private Long themeId;

    public ArticleRequest() {}

    public ArticleRequest(String title, String content, Long themeId) {
        this.title = title;
        this.content = content;
        this.themeId = themeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }
}
