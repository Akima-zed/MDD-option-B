package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration Spring Security pour l'application MDD.
 * Définit les règles de sécurité, l'authentification JWT et le hachage des mots de passe.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Bean pour le hachage des mots de passe avec BCrypt.
     * BCrypt utilise un salt aléatoire et est résistant aux attaques par force brute.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration de la chaîne de filtres de sécurité.
     * Définit quelles routes sont publiques et lesquelles nécessitent une authentification.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF car on utilise des tokens JWT (API REST stateless)
            .csrf().disable()
            
            // Configuration des autorisations
            .authorizeHttpRequests(authorize -> authorize
                // Routes publiques (accessibles sans authentification)
                .antMatchers("/api/auth/**").permitAll()
                
                // Toutes les autres routes nécessitent une authentification
                .anyRequest().authenticated()
            )
            
            // Politique de session: STATELESS (pas de session HTTP, tout est géré par JWT)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            
            // Ajouter notre filtre JWT avant le filtre d'authentification standard
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
