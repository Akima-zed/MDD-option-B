package com.openclassrooms.mddapi.dto;

import java.time.LocalDate;
import java.util.Set;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private LocalDate dateInscription;
    private Set<ThemeResponse> abonnements;

    public UserResponse(Long id,
                        String username,
                        String email,
                        LocalDate dateInscription,
                        Set<ThemeResponse> abonnements) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.dateInscription = dateInscription;
        this.abonnements = abonnements;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public LocalDate getDateInscription() { return dateInscription; }
    public Set<ThemeResponse> getAbonnements() { return abonnements; }
}