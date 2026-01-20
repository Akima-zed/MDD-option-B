package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.model.*;
import com.openclassrooms.mddapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Classe utilisée uniquement en développement pour insérer
 * quelques données de test au démarrage de l'application.
 * Elle est désactivée pendant les tests grâce au profil "!test".
 */


@Configuration
@Profile("!test")
public class DataInitializer {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Bean
    CommandLineRunner initData(UserRepository userRepo, ThemeRepository themeRepo, ArticleRepository articleRepo, CommentRepository commentRepo) {
                return args -> {

            // Ajout de thèmes si la table est vide
            if (themeRepo.count() == 0) {
                String lorem = "Description :Le lorem ipsum (également appelé faux-texte, lipsum, ou bolo bolo[1]) est, en imprimerie, une suite de mots sans signification utilisée à titre provisoire pour calibrer une mise en page.";

                Theme java = new Theme();
                java.setNom("Java");
                java.setDescription(lorem);

                Theme angular = new Theme();
                angular.setNom("Angular");
                angular.setDescription(lorem);
                themeRepo.save(java);
                themeRepo.save(angular);

                


            }

            // Ajout d'un utilisateur de test
            if (userRepo.count() == 0) {
                User user = new User();
                user.setUsername("johndoe");
                user.setEmail("john@example.com");

                //Mot de passe conforme à lasécurité
                user.setPassword(passwordEncoder.encode("Password123!"));
                user.setRoles(Collections.singleton("USER"));
                user.setDateInscription(LocalDate.now());
                userRepo.save(user);
            }

            // Ajout d'un article de test
            if (articleRepo.count() == 0) {
                User user = userRepo.findAll().get(0);
                Theme java = themeRepo.findAll().stream().filter(t -> t.getNom().equals("Java")).findFirst().orElse(null);
                Article article = new Article();
                article.setTitle("Premier article");
                article.setContent("Contenu test de l'article.");
                article.setAuthor(user);
                article.setTheme(java);
                article.setCreatedAt(LocalDateTime.now());
                articleRepo.save(article);
            }

            // Ajout d'un commentaire de test
            if (commentRepo.count() == 0) {
                User user = userRepo.findAll().get(0);
                Article article = articleRepo.findAll().get(0);
                Comment comment = new Comment();
                comment.setContent("Bravo pour cet article !");
                comment.setAuthor(user);
                comment.setArticle(article);
                comment.setDateCreation(LocalDate.now());
                commentRepo.save(comment);
            }
        };
    }
}
