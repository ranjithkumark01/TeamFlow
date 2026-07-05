package com.teamflow.dto.response;

import com.teamflow.entity.enums.NotificationType;
import java.time.OffsetDateTime;

public record NotificationResponse(
        Long id,
        Long recipientId,
        String title,
        String message,
        NotificationType type,
        boolean read,
        OffsetDateTime createdAt,
        OffsetDateTime readAt
) {
}

