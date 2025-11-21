package com.ismail.authentification.dto;

import com.ismail.authentification.model.enums.Role;

public record AuthResponseDto(
        String username,
        Role role,
        String token
) {
}
