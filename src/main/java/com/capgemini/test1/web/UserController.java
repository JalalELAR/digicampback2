package com.capgemini.test1.web;

import com.capgemini.test1.dtos.RegisterUserDto;
import com.capgemini.test1.dtos.UpdatePasswordDto;
import com.capgemini.test1.entities.User;
import com.capgemini.test1.service.userservice.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Route pour mettre à jour un utilisateur par son ID (administrateurs uniquement)
    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        // Ne permettez pas la mise à jour si l'utilisateur courant n'est pas un admin
        //User currentUser = getCurrentUser();
        return ResponseEntity.ok(userService.updateUserById(id, updatedUser));
    }

    // Route pour récupérer l'utilisateur connecté
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    // Route pour récupérer tous les utilisateurs (administrateurs uniquement)
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    // Route pour supprimer un utilisateur par son ID (administrateurs uniquement)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        userService.deleteUserById(id);
        return ResponseEntity.ok("L'utilisateur avec l'ID " + id + " a été supprimé avec succès.");
    }
    // Nouvelle route pour chercher un utilisateur par son ID (administrateurs uniquement)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> findUserById(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        User user = userService.findUserById(id)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me/update-password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        Integer userId = getCurrentUser().getId();
        userService.updatePassword(userId, updatePasswordDto.getCurrentPassword(), updatePasswordDto.getNewPassword());
        return ResponseEntity.ok("Mot de passe mis à jour avec succès !");
    }


    @PostMapping("/adding-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> addUser(@RequestBody RegisterUserDto registerUserDto) {
        User newUser = userService.signup(registerUserDto);
        return ResponseEntity.ok(newUser);
    }

    // Méthode utilitaire pour obtenir l'utilisateur actuellement authentifié
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
