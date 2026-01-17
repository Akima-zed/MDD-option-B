package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


/**
 * Configuration principale de la sécurité :
 * - CORS pour autoriser le front Angular
 * - Désactivation CSRF (API REST)
 * - Définition des routes publiques
 * - Activation du filtre JWT
 * - Mode stateless pour l'API
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Autorise les requêtes venant du front Angular
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true);
                config.setAllowedOrigins(List.of("http://localhost:4200"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                return config;
            }))

            // Désactivation CSRF car l'API utilise JWT
            .csrf(csrf -> csrf.disable())

            // Définition des routes publiques et protégées
            .authorizeHttpRequests(auth -> auth
            .mvcMatchers("/api/auth/**").permitAll()
            .mvcMatchers(HttpMethod.GET, "/api/articles/**").permitAll()
            .mvcMatchers(HttpMethod.GET, "/api/themes/**").permitAll()
            .anyRequest().authenticated()
        )

            //Mode stateless : aucune session côté serveur
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Ajout du filtre JWT avant le filtre d'authentification standard
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}