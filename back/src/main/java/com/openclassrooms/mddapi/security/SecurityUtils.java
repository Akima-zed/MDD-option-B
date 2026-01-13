package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * Retourne l'ID de l'utilisateur actuellement authentifié.
     * Retourne null si aucun utilisateur n'est connecté.
     */
    public static Long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || auth.getPrincipal() == null) {
        return null;
    }

    Object principal = auth.getPrincipal();

    if (principal instanceof User) {
        return ((User) principal).getId();
    }

    return null;
}
}