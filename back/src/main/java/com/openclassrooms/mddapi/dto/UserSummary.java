package com.openclassrooms.mddapi.dto;

public class UserSummary {
    private Long id;
    private String username;

    public UserSummary(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
}