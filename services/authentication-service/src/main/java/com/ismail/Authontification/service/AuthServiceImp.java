package com.ismail.Authontification.service;

import com.ismail.Authontification.model.User;
import com.ismail.Authontification.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {

    private final AuthRepository authRepository;

    public AuthServiceImp(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public User registerUser(User user) {
        return authRepository.save(user);
    }

    @Override
    public Boolean isExistingUser(String email) {
        return null;
    }

    @Override
    public User loginUser(String email, String password) {
        return authRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return authRepository.findAll();
    }
}
