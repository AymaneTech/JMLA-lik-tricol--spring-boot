package com.ismail.Authontification.service;

import com.ismail.Authontification.model.User;

import java.util.List;

public interface AuthService {

    public User registerUser(User user);

    public Boolean isExistingUser(String email);

    public User loginUser(String email, String password);

    public List<User> getAllUsers();
}
