package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

/**
 * Tests unitaires du ArticleService utilisant Mockito.
 */
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    private Article testArticle;
    private User testUser;
    private Theme testTheme;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setNom("Java");

        testArticle = new Article();
        testArticle.setId(1L);
        testArticle.setTitle("Test Article");
        testArticle.setContent("Test Content");
        testArticle.setAuthor(testUser);
        testArticle.setTheme(testTheme);
    }

    @Test
    @DisplayName("Doit retourner tous les articles")
    void testFindAll() {
        // ÉTANT DONNÉ
        List<Article> articles = Arrays.asList(testArticle);
        when(articleRepository.findAll()).thenReturn(articles);

        // QUAND
        List<Article> result = articleService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Article", result.get(0).getTitle());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Doit retourner les articles triés par date")
    void testFindAllOrderByCreatedAtDesc() {
        // ÉTANT DONNÉ
        List<Article> articles = Arrays.asList(testArticle);
        when(articleRepository.findAllByOrderByCreatedAtDesc()).thenReturn(articles);

        // QUAND
        List<Article> result = articleService.findAllOrderByCreatedAtDesc();

        // ALORS
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(articleRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Doit retourner l'article si trouvé")
    void testFindById_Success() {
        // ÉTANT DONNÉ
        when(articleRepository.findById(1L)).thenReturn(Optional.of(testArticle));

        // QUAND
        Optional<Article> result = articleService.findById(1L);

        // ALORS
        assertTrue(result.isPresent());
        assertEquals("Test Article", result.get().getTitle());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Doit retourner Optional.empty si non trouvé")
    void testFindById_NotFound() {
        // ÉTANT DONNÉ
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        // QUAND
        Optional<Article> result = articleService.findById(999L);

        // ALORS
        assertFalse(result.isPresent());
        verify(articleRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Doit enregistrer un article avec succès")
    void testSave() {
        // ÉTANT DONNÉ
        when(articleRepository.save(any(Article.class))).thenReturn(testArticle);

        // QUAND
        Article result = articleService.save(testArticle);

        // ALORS
        assertNotNull(result);
        assertEquals("Test Article", result.getTitle());
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    @DisplayName("Doit supprimer un article")
    void testDeleteById() {
        // QUAND
        articleService.deleteById(1L);

        // ALORS
        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Doit retourner une liste vide")
    void testFindAll_Empty() {
        // ÉTANT DONNÉ
        when(articleRepository.findAll()).thenReturn(Arrays.asList());

        // QUAND
        List<Article> result = articleService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Doit enregistrer plusieurs articles")
    void testSaveMultipleArticles() {
        // ÉTANT DONNÉ
        Article article2 = new Article();
        article2.setId(2L);
        article2.setTitle("Second Article");
        article2.setContent("Second Content");

        when(articleRepository.save(any(Article.class)))
                .thenReturn(testArticle)
                .thenReturn(article2);

        // QUAND
        Article result1 = articleService.save(testArticle);
        Article result2 = articleService.save(article2);

        // ALORS
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals("Test Article", result1.getTitle());
        assertEquals("Second Article", result2.getTitle());
        verify(articleRepository, times(2)).save(any(Article.class));
    }

    @Test
    @DisplayName("Doit retourner plusieurs articles")
    void testFindAll_MultipleArticles() {
        // ÉTANT DONNÉ
        Article article2 = new Article();
        article2.setId(2L);
        article2.setTitle("Second Article");

        List<Article> articles = Arrays.asList(testArticle, article2);
        when(articleRepository.findAll()).thenReturn(articles);

        // QUAND
        List<Article> result = articleService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Article", result.get(0).getTitle());
        assertEquals("Second Article", result.get(1).getTitle());
        verify(articleRepository, times(1)).findAll();
    }
}
