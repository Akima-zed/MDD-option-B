package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThemeResponse {
    private Long id;

    @JsonProperty("name")
    private String nom;

    private String description;

     private boolean subscribed;

    public ThemeResponse(Long id, String nom, String description, boolean subscribed) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.subscribed = subscribed;

    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public boolean isSubscribed() { return subscribed; }

}