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
}
