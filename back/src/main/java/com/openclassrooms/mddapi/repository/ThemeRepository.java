package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository permettant d'accéder aux thèmes disponibles.
 */
public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
