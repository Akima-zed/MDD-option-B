package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserUpdateRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du UserService utilisant Mockito.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("johndoe");
        testUser.setEmail("john@test.com");
        testUser.setPassword("hashedPassword123");
    }

    @Test
    @DisplayName("Doit enregistrer un utilisateur avec succès")
    void testSave() {
        // ÉTANT DONNÉ
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // QUAND
        User savedUser = userService.save(testUser);

        // ALORS
        assertNotNull(savedUser);
        assertEquals("johndoe", savedUser.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Doit retrouver un utilisateur par email")
    void testFindByEmail() {
        // ÉTANT DONNÉ
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testUser));

        // QUAND
        Optional<User> foundUser = userService.findByEmail("john@test.com");

        // ALORS
        assertTrue(foundUser.isPresent());
        assertEquals("john@test.com", foundUser.get().getEmail());
        verify(userRepository, times(1)).findByEmail("john@test.com");
    }

    @Test
    @DisplayName("Doit retourner vide si aucun utilisateur trouvé par email")
    void testFindByEmail_NotFound() {
        // ÉTANT DONNÉ
        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        // QUAND
        Optional<User> foundUser = userService.findByEmail("notfound@test.com");

        // ALORS
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Doit retrouver un utilisateur par username")
    void testFindByUsername() {
        // ÉTANT DONNÉ
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(testUser));

        // QUAND
        Optional<User> foundUser = userService.findByUsername("johndoe");

        // ALORS
        assertTrue(foundUser.isPresent());
        assertEquals("johndoe", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Doit vérifier si un email existe déjà")
    void testExistsByEmail() {
        // ÉTANT DONNÉ
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testUser));

        // QUAND
        boolean exists = userService.existsByEmail("john@test.com");

        // ALORS
        assertTrue(exists);
    }

        @Test
    @DisplayName("Doit retourner false si l'email n'existe pas")
    void testExistsByEmail_NotExists() {
        // ÉTANT DONNÉ
        when(userRepository.findByEmail("nonexistent@test.com"))
                .thenReturn(Optional.empty());

        // QUAND
        boolean exists = userService.existsByEmail("nonexistent@test.com");

        // ALORS
        assertFalse(exists);
    }

    @Test
    @DisplayName("Doit vérifier si un username existe déjà")
    void testExistsByUsername() {
        // ÉTANT DONNÉ
        when(userRepository.findByUsername("johndoe"))
                .thenReturn(Optional.of(testUser));

        // QUAND
        boolean exists = userService.existsByUsername("johndoe");

        // ALORS
        assertTrue(exists);
    }

    @Test
    @DisplayName("Doit retrouver un utilisateur par son ID")
    void testFindById() {
        // ÉTANT DONNÉ
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(testUser));

        // QUAND
        Optional<User> foundUser = userService.findById(1L);

        // ALORS
        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
    }

    // ==================== Tests pour update() ====================

    @Test
    @DisplayName("Doit mettre à jour un utilisateur avec succès")
    void testUpdate_Success() {
        // ÉTANT DONNÉ
        UserUpdateRequest updatedUserData = new UserUpdateRequest();
        updatedUserData.setUsername("john_updated");
        updatedUserData.setEmail("john.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("john.updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("john_updated")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // QUAND
        User result = userService.update(1L, updatedUserData);

        // ALORS
        assertNotNull(result);
        assertEquals("john_updated", result.getUsername());
        assertEquals("john.updated@example.com", result.getEmail());
        assertEquals("hashedPassword123", result.getPassword()); // inchangé
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Doit lever une exception si l'utilisateur n'existe pas lors de la mise à jour")
    void testUpdate_UserNotFound() {
        // ÉTANT DONNÉ
        UserUpdateRequest updatedUserData = new UserUpdateRequest();
        updatedUserData.setUsername("john_updated");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // QUAND & ALORS
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.update(999L, updatedUserData)
        );

        assertEquals("Utilisateur non trouvé avec l'ID: 999", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Doit lever une exception si l'email est déjà utilisé")
    void testUpdate_EmailAlreadyExists() {
        // ÉTANT DONNÉ
        UserUpdateRequest updatedUserData = new UserUpdateRequest();
        updatedUserData.setEmail("jane@example.com"); // email déjà pris

        // Simule un autre utilisateur possédant cet email
        User autreUtilisateur = new User();
        autreUtilisateur.setId(2L);
        autreUtilisateur.setEmail("jane@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(autreUtilisateur));

        // QUAND & ALORS
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.update(1L, updatedUserData)
        );

        assertEquals("Cet email est déjà utilisé", exception.getMessage());
        verify(userRepository, never()).save(any());
    }


        @Test
    @DisplayName("Doit lever une exception si le username est déjà utilisé")
    void testUpdate_UsernameAlreadyExists() {
        // ÉTANT DONNÉ
        User autreUtilisateur = new User();
        autreUtilisateur.setId(2L);
        autreUtilisateur.setUsername("jane_doe");

        UserUpdateRequest updatedUserData = new UserUpdateRequest();
        updatedUserData.setUsername("jane_doe"); // Username déjà pris

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("jane_doe")).thenReturn(Optional.of(autreUtilisateur));

        // QUAND & ALORS
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.update(1L, updatedUserData)
        );

        assertEquals("Ce nom d'utilisateur est déjà utilisé", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Doit permettre à l'utilisateur de conserver son propre email")
    void testUpdate_SameEmailKeepsSameUser() {
        // ÉTANT DONNÉ — l'utilisateur garde son email actuel
        UserUpdateRequest updatedUserData = new UserUpdateRequest();
        updatedUserData.setEmail("john@test.com"); // même email
        updatedUserData.setUsername("john_updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("john_updated")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // QUAND
        User result = userService.update(1L, updatedUserData);

        // ALORS
        assertNotNull(result);
        assertEquals("john_updated", result.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Doit mettre à jour uniquement le username si l'email est null")
    void testUpdate_OnlyUsernameChanged() {
        // ÉTANT DONNÉ — seul le username change
        UserUpdateRequest updatedUserData = new UserUpdateRequest();
        updatedUserData.setUsername("john_new");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("john_new")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // QUAND
        User result = userService.update(1L, updatedUserData);

        // ALORS
        assertNotNull(result);
        assertEquals("john_new", result.getUsername());
        assertEquals("john@test.com", result.getEmail()); // email inchangé
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Doit mettre à jour uniquement l'email si le username est null")
    void testUpdate_OnlyEmailChanged() {
        // ÉTANT DONNÉ — seul l'email change
        UserUpdateRequest updatedUserData = new UserUpdateRequest();
        updatedUserData.setEmail("new.email@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("new.email@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // QUAND
        User result = userService.update(1L, updatedUserData);

        // ALORS
        assertNotNull(result);
        assertEquals("new.email@example.com", result.getEmail());
        assertEquals("johndoe", result.getUsername()); // username inchangé
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Doit retourner plusieurs utilisateurs")
    void testFindAll_MultipleUsers() {
        // ÉTANT DONNÉ
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("janedoe");
        user2.setEmail("jane@test.com");
        user2.setPassword("hashedPassword456");

        List<User> users = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);

        // QUAND
        List<User> result = userService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("johndoe", result.get(0).getUsername());
        assertEquals("janedoe", result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Doit retourner une liste vide")
    void testFindAll_Empty() {
        // ÉTANT DONNÉ
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // QUAND
        List<User> result = userService.findAll();

        // ALORS
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository, times(1)).findAll();
    }
}