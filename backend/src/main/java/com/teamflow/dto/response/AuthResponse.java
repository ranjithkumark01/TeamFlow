package com.teamflow.dto.response;

import com.teamflow.entity.enums.UserRole;

public record AuthResponse(
        String tokenType,
        String accessToken,
        Long expiresInMs,
        Long userId,
        String name,
        String email,
        UserRole role
) {
}

