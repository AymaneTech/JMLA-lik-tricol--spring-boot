package com.ismail.authentification.controller;

import com.ismail.authentification.dto.AuthResponseDto;
import com.ismail.authentification.dto.LoginDTO;
import com.ismail.authentification.dto.RegisterDTO;
import com.ismail.authentification.model.User;
import com.ismail.authentification.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Tag(name = "Register User", description = "Create a new user account")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> save(@RequestBody RegisterDTO registerDTO) {
        User savedUser = authService.registerUser(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToResponse(savedUser));
    }

    @Tag(name = "Login", description = "Authenticate user and get user details")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDTO loginDto) {
        User user = authService.loginUser(loginDto);
        return ResponseEntity.ok(mapToResponse(user));
    }


    @Tag(name = "Get All Users", description = "Retrieve a list of all registered users")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    private AuthResponseDto mapToResponse(User user) {
        return new AuthResponseDto(user.getUserName(), user.getRole(), user.getId());
    }
}
