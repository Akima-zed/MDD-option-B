package com.openclassrooms.mddapi.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour UserService avec Mockito
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
    @DisplayName("Should save user successfully")
    void testSave() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User savedUser = userService.save(testUser);

        // Then
        assertNotNull(savedUser);
        assertEquals("johndoe", savedUser.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByEmail() {
        // Given
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> foundUser = userService.findByEmail("john@test.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("john@test.com", foundUser.get().getEmail());
        verify(userRepository, times(1)).findByEmail("john@test.com");
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void testFindByEmail_NotFound() {
        // Given
        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userService.findByEmail("notfound@test.com");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindByUsername() {
        // Given
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> foundUser = userService.findByUsername("johndoe");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("johndoe", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should check if email exists")
    void testExistsByEmail() {
        // Given
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testUser));

        // When
        boolean exists = userService.existsByEmail("john@test.com");

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false if email does not exist")
    void testExistsByEmail_NotExists() {
        // Given
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // When
        boolean exists = userService.existsByEmail("nonexistent@test.com");

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should check if username exists")
    void testExistsByUsername() {
        // Given
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(testUser));

        // When
        boolean exists = userService.existsByUsername("johndoe");

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should find user by ID")
    void testFindById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> foundUser = userService.findById(1L);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
    }

    // ==================== Tests pour update() ====================

    @Test
    @DisplayName("Should update user successfully")
    void testUpdate_Success() {
        // Given
        User updatedUserData = new User();
        updatedUserData.setUsername("john_updated");
        updatedUserData.setEmail("john.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("john.updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("john_updated")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.update(1L, updatedUserData);

        // Then
        assertNotNull(result);
        assertEquals("john_updated", result.getUsername());
        assertEquals("john.updated@example.com", result.getEmail());
        assertEquals("hashedPassword123", result.getPassword()); // Le mot de passe ne change pas
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should throw exception when user not found during update")
    void testUpdate_UserNotFound() {
        // Given
        User updatedUserData = new User();
        updatedUserData.setUsername("john_updated");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.update(999L, updatedUserData);
        });

        assertEquals("Utilisateur non trouvé avec l'ID: 999", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testUpdate_EmailAlreadyExists() {
        // Given
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail("jane@example.com");

        User updatedUserData = new User();
        updatedUserData.setEmail("jane@example.com"); // Email déjà pris par user ID=2

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(anotherUser));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.update(1L, updatedUserData);
        });

        assertEquals("Cet email est déjà utilisé", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void testUpdate_UsernameAlreadyExists() {
        // Given
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("jane_doe");

        User updatedUserData = new User();
        updatedUserData.setUsername("jane_doe"); // Username déjà pris par user ID=2

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("jane_doe")).thenReturn(Optional.of(anotherUser));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.update(1L, updatedUserData);
        });

        assertEquals("Ce nom d'utilisateur est déjà utilisé", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow user to keep same email")
    void testUpdate_SameEmailKeepsSameUser() {
        // Given - L'utilisateur garde son propre email
        User updatedUserData = new User();
        updatedUserData.setEmail("john@test.com"); // Même email qu'avant
        updatedUserData.setUsername("john_updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("john_updated")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.update(1L, updatedUserData);

        // Then
        assertNotNull(result);
        assertEquals("john_updated", result.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should update only username when email is null")
    void testUpdate_OnlyUsernameChanged() {
        // Given - Modification uniquement du username
        User updatedUserData = new User();
        updatedUserData.setUsername("john_new");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("john_new")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.update(1L, updatedUserData);

        // Then
        assertNotNull(result);
        assertEquals("john_new", result.getUsername());
        assertEquals("john@test.com", result.getEmail()); // Email inchangé
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should update only email when username is null")
    void testUpdate_OnlyEmailChanged() {
        // Given - Modification uniquement de l'email
        User updatedUserData = new User();
        updatedUserData.setEmail("new.email@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("new.email@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.update(1L, updatedUserData);

        // Then
        assertNotNull(result);
        assertEquals("new.email@example.com", result.getEmail());
        assertEquals("johndoe", result.getUsername()); // Username inchangé
        verify(userRepository, times(1)).save(testUser);
    }
}
