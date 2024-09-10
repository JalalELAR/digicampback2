package com.capgemini.test1.service.userservice;

import com.capgemini.test1.dtos.RegisterUserDto;
import com.capgemini.test1.entities.User;
import com.capgemini.test1.repositories.UserRepository;
import com.capgemini.test1.service.serviceSecurity.EmailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;  // Injecter le service d'email


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;  // Initialiser le service d'email
    }


    public List<User> allUsers() {
        return userRepository.findAll();
    }

    // Méthode pour chercher un utilisateur par ID
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    /* Méthode pour mettre à jour un utilisateur par ID
    public User updateUserById(Integer id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFullName(updatedUser.getFullName());
                    user.setEmail(updatedUser.getEmail());
                    // Ne pas modifier le mot de passe ici
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));
    }*/
    // Method to update a user by ID
    // Method to update a user by ID
    public User updateUserById(Integer id, User updatedUser) {
        // Log the update attempt
        logger.info("Attempting to update user with ID: {}", id);

        return userRepository.findById(id)
                .map(user -> {
                    // Check and log the update
                    if (updatedUser.getFullName() != null) {
                        user.setFullName(updatedUser.getFullName());
                        logger.info("Updated user full name to: {}", updatedUser.getFullName());
                    }
                    if (updatedUser.getEmail() != null) {
                        user.setEmail(updatedUser.getEmail());
                        logger.info("Updated user email to: {}", updatedUser.getEmail());
                    }
                    if (updatedUser.getRole() != null) {
                        user.setRole(updatedUser.getRole());
                        logger.info("Updated user role to: {}", updatedUser.getRole());
                    }
                    // Do not modify the password here
                    return userRepository.save(user);
                })
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", id);
                    return new IllegalStateException("Utilisateur non trouvé");
                });
    }

    // Méthode pour supprimer un utilisateur par ID
    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));

        userRepository.delete(user);
    }

    public void updatePassword(Integer userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));

        // Vérifie si le mot de passe actuel correspond
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe actuel incorrect");
        }

        // Encode et met à jour le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User signup(RegisterUserDto registerUserDto) {

        if (registerUserDto.getEmail() == null || registerUserDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Le mail ne peut être nul ou vide");
        }

        if (registerUserDto.getFullName() == null || registerUserDto.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être nul ou vide");
        }

        if (userRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
            throw new IllegalStateException("Cet email est déjà utilisé");
        }

        // Générer un mot de passe aléatoire
        String generatedPassword = generateRandomPassword();

        // Créer un nouvel utilisateur
        User newUser = new User();
        newUser.setFullName(registerUserDto.getFullName());
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(generatedPassword));
        newUser.setRole("USER");

        // Enregistrer l'utilisateur
        User savedUser = userRepository.save(newUser);

        // Envoyer un email avec le mot de passe généré
        String subject = "Bienvenue sur DigiCamp !";
        String text = String.format("Bonjour %s,\n\nVotre compte sur DigiCamp a été créé avec succès.\n\nVotre mot de passe est: %s\n\nMerci de le changer lors de votre première connexion.",
                newUser.getFullName(), generatedPassword);
        emailService.sendEmail(newUser.getEmail(), subject, text);

        return savedUser;
    }

    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10); // Génère un mot de passe de 10 caractères
    }

}
