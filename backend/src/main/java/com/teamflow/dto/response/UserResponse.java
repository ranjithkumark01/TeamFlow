package com.teamflow.dto.response;

import com.teamflow.entity.enums.UserRole;
import java.time.OffsetDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

