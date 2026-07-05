package com.teamflow.dto.response;

import java.time.OffsetDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        Long createdById,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

