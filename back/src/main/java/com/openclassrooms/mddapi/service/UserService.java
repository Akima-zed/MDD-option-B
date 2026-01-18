package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service gérant les opérations liées aux utilisateurs.
 * Les méthodes exposent les actions CRUD nécessaires au projet.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Met à jour les informations d'un utilisateur (username et email uniquement).
     * Le mot de passe ne peut pas être modifié via cette méthode.
     * 
     * @param id L'ID de l'utilisateur à modifier
     * @param updatedUser Les nouvelles informations (username, email)
     * @return L'utilisateur mis à jour
     * @throws RuntimeException si l'utilisateur n'existe pas ou si l'email est déjà utilisé
     */
    public User update(Long id, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        
        if (!existingUserOpt.isPresent()) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + id);
        }
        
        User existingUser = existingUserOpt.get();
        
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            Optional<User> userWithEmail = userRepository.findByEmail(updatedUser.getEmail());
            if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(id)) {
                throw new RuntimeException("Cet email est déjà utilisé");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }
        
        // Vérifier si le username est déjà utilisé par un autre utilisateur
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())) {
            Optional<User> userWithUsername = userRepository.findByUsername(updatedUser.getUsername());
            if (userWithUsername.isPresent() && !userWithUsername.get().getId().equals(id)) {
                throw new RuntimeException("Ce nom d'utilisateur est déjà utilisé");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }
        
        return userRepository.save(existingUser);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
