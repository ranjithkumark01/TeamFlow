package com.teamflow.dto.response;

import java.time.OffsetDateTime;

public record CommentResponse(
        Long id,
        Long taskId,
        Long authorId,
        String content,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

