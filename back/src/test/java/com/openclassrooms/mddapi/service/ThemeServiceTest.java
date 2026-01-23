package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
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
 * Tests unitaires du ThemeService utilisant Mockito.
 */
@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    private Theme testTheme;

    @BeforeEach
    void setUp() {
        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setNom("Java");
        testTheme.setDescription("Java programming language");
    }

    @Test
    @DisplayName("findAll - Doit retourner tous les thèmes")
    void testFindAll() {
        // ÉTANT DONNÉ
        List<Theme> themes = Arrays.asList(testTheme);
        when(themeRepository.findAll()).thenReturn(themes);

        // QUAND
        List<Theme> result = themeService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getNom());
        verify(themeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById - Doit retourner le thème si trouvé")
    void testFindById_Success() {
        // ÉTANT DONNÉ
        when(themeRepository.findById(1L)).thenReturn(Optional.of(testTheme));

        // QUAND
        Optional<Theme> result = themeService.findById(1L);

        // ALORS
        assertTrue(result.isPresent());
        assertEquals("Java", result.get().getNom());
        verify(themeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById - Doit retourner Optional.empty si non trouvé")
    void testFindById_NotFound() {
        // ÉTANT DONNÉ
        when(themeRepository.findById(999L)).thenReturn(Optional.empty());

        // QUAND
        Optional<Theme> result = themeService.findById(999L);

        // ALORS
        assertFalse(result.isPresent());
        verify(themeRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("save - Doit enregistrer un thème avec succès")
    void testSave() {
        // ÉTANT DONNÉ
        when(themeRepository.save(any(Theme.class))).thenReturn(testTheme);

        // QUAND
        Theme result = themeService.save(testTheme);

        // ALORS
        assertNotNull(result);
        assertEquals("Java", result.getNom());
        verify(themeRepository, times(1)).save(any(Theme.class));
    }

    @Test
    @DisplayName("findAll - Doit retourner une liste vide")
    void testFindAll_Empty() {
        // ÉTANT DONNÉ
        when(themeRepository.findAll()).thenReturn(Arrays.asList());

        // QUAND
        List<Theme> result = themeService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(themeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("deleteById - Doit supprimer un thème")
    void testDeleteById() {
        // QUAND
        themeService.deleteById(1L);

        // ALORS
        verify(themeRepository, times(1)).deleteById(1L);
    }
}
