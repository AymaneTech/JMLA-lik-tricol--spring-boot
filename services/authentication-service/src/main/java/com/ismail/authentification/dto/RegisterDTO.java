package com.ismail.authentification.dto;

import com.ismail.authentification.model.enums.Role;

public record RegisterDTO(
        String userName,
        String email,
        Role role,
        String password
) {
}
