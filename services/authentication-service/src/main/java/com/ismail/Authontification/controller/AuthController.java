package com.ismail.Authontification.controller;

import com.ismail.Authontification.dto.AuthResponseDto;
import com.ismail.Authontification.dto.LoginDTO;
import com.ismail.Authontification.dto.UserDTO;
import com.ismail.Authontification.model.User;
import com.ismail.Authontification.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Tag(name = "Register User", description = "Create a new user account")
    @PostMapping("/register")
    public ResponseEntity<?> save(@RequestBody User user) {
        try {
            user.setId(UUID.randomUUID().toString());
            User savedUser = authService.registerUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponseDto(savedUser.getUserName(), savedUser.getRole(), savedUser.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur: " + e.getMessage());
        }
    }

    @Tag(name = "Login", description = "Authenticate user and get user details")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            User user = authService.loginUser(loginDto.getEmail(), loginDto.getPassword());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Email ou mot de passe incorrect");
            }

            AuthResponseDto authResponseDto = new AuthResponseDto(user.getUserName(), user.getRole(), user.getId());
            return ResponseEntity.ok(authResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la connexion: " + e.getMessage());
        }
    }


    @Tag(name = "Get All Users", description = "Retrieve a list of all registered users")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(authService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur: " + e.getMessage());
        }
    }
}
