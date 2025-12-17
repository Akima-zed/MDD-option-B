package com.openclassrooms.mddapi.dto;

import javax.validation.constraints.NotBlank;

/**
 * DTO pour la requête de connexion.
 */
public class LoginRequest {
    
    @NotBlank(message = "L'email ou nom d'utilisateur est obligatoire")
    private String emailOrUsername;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String emailOrUsername, String password) {
        this.emailOrUsername = emailOrUsername;
        this.password = password;
    }

    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
