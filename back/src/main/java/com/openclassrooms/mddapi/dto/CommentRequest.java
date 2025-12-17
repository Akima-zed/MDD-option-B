package com.openclassrooms.mddapi.dto;

import javax.validation.constraints.NotBlank;

/**
 * DTO pour la création d'un nouveau commentaire.
 */
public class CommentRequest {
    
    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    private String content;

    public CommentRequest() {}

    public CommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
