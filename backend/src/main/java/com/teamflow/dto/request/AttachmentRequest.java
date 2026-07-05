package com.teamflow.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttachmentRequest(
        @NotNull Long taskId,
        @NotNull Long uploadedById,
        @NotBlank @Size(max = 255) String fileName,
        @NotBlank String fileUrl,
        @Size(max = 120) String contentType,
        @Min(0) Long fileSize
) {
}

