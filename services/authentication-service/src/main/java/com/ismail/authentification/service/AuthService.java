package com.ismail.authentification.service;

import com.ismail.authentification.dto.LoginDTO;
import com.ismail.authentification.dto.RegisterDTO;
import com.ismail.authentification.model.User;

import java.util.List;

public interface AuthService {

    User registerUser(RegisterDTO registerDTO);

    Boolean isExistingUser(String email);

    User loginUser(LoginDTO loginDto);

    List<User> getAllUsers();
}
