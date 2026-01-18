package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Classe utilitaire permettant de récupérer les informations
 * de l'utilisateur actuellement authentifié via Spring Security.
 */
public class SecurityUtils {

    /**
     * Retourne l'ID de l'utilisateur actuellement authentifié / connecté .
     * Retourne null si aucun utilisateur n'est connecté.
     */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            return null;
    }
        // principal est l'objet UserDetails personnalisé
        Object principal = auth.getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getId();
    }

    return null;
}
}