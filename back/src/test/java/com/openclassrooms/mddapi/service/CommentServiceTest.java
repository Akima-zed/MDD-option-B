package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du CommentService utilisant Mockito.
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment testComment;
    private User testUser;
    private Article testArticle;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testArticle = new Article();
        testArticle.setId(1L);
        testArticle.setTitle("Test Article");

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Test comment");
        testComment.setAuthor(testUser);
        testComment.setArticle(testArticle);
    }

    @Test
    @DisplayName("findAll - Doit retourner tous les commentaires")
    void testFindAll() {
        // ÉTANT DONNÉ
        List<Comment> comments = Arrays.asList(testComment);
        when(commentRepository.findAll()).thenReturn(comments);

        // QUAND
        List<Comment> result = commentService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getContent());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById - Doit retourner le commentaire si trouvé")
    void testFindById_Success() {
        // ÉTANT DONNÉ
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // QUAND
        Optional<Comment> result = commentService.findById(1L);

        // ALORS
        assertTrue(result.isPresent());
        assertEquals("Test comment", result.get().getContent());
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById - Doit retourner Optional.empty si non trouvé")
    void testFindById_NotFound() {
        // ÉTANT DONNÉ
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // QUAND
        Optional<Comment> result = commentService.findById(999L);

        // ALORS
        assertFalse(result.isPresent());
        verify(commentRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("save - Doit enregistrer un commentaire avec succès")
    void testSave() {
        // ÉTANT DONNÉ
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // QUAND
        Comment result = commentService.save(testComment);

        // ALORS
        assertNotNull(result);
        assertEquals("Test comment", result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("deleteById - Doit supprimer un commentaire")
    void testDeleteById() {
        // QUAND
        commentService.deleteById(1L);

        // ALORS
        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findAll - Doit retourner une liste vide")
    void testFindAll_Empty() {
        // ÉTANT DONNÉ
        when(commentRepository.findAll()).thenReturn(Arrays.asList());

        // QUAND
        List<Comment> result = commentService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(commentRepository, times(1)).findAll();
    }
}
