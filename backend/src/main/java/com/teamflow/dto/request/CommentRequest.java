package com.teamflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull Long taskId,
        @NotNull Long authorId,
        @NotBlank String content
) {
}

