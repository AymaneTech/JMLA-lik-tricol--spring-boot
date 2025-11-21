package com.ismail.authentification.service;

import com.ismail.authentification.dto.LoginDTO;
import com.ismail.authentification.dto.RegisterDTO;
import com.ismail.authentification.exception.IncorrectLoginCredentialsException;
import com.ismail.authentification.model.User;
import com.ismail.authentification.repository.AuthRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements AuthService {

    private final AuthRepository authRepository;

    public DefaultUserService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public User registerUser(RegisterDTO registerDTO) {
        // TODO: validation
        var user = new User(
                registerDTO.email(),
                registerDTO.userName(),
                registerDTO.password(),
                registerDTO.role()
        );

        // TODO: create custom dto for response
        return authRepository.save(user);
    }

    @Override
    public Boolean isExistingUser(String email) {
        return authRepository.existsByEmail(email);
    }

    @Override
    public User loginUser(LoginDTO loginDTO) {
        return authRepository.findByEmailAndPassword(loginDTO.email(), loginDTO.password())
                .orElseThrow(() -> new IncorrectLoginCredentialsException("Email ou mot de passe incorrect"));
    }

    @Override
    public List<User> getAllUsers() {
        return authRepository.findAll();
    }
}
