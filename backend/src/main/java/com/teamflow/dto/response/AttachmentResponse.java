package com.teamflow.dto.response;

import java.time.OffsetDateTime;

public record AttachmentResponse(
        Long id,
        Long taskId,
        Long uploadedById,
        String fileName,
        String fileUrl,
        String contentType,
        Long fileSize,
        OffsetDateTime uploadedAt
) {
}

