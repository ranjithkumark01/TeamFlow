package com.teamflow.dto.request;

import com.teamflow.entity.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record NotificationRequest(
        @NotNull Long recipientId,
        @NotBlank @Size(max = 160) String title,
        @NotBlank String message,
        @NotNull NotificationType type,
        boolean read,
        OffsetDateTime readAt
) {
}

