package com.teamflow.mapper;

import com.teamflow.dto.request.NotificationRequest;
import com.teamflow.dto.response.NotificationResponse;
import com.teamflow.entity.Notification;
import com.teamflow.entity.User;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static Notification toEntity(NotificationRequest request, User recipient) {
        Notification notification = new Notification();
        updateEntity(notification, request, recipient);
        return notification;
    }

    public static void updateEntity(Notification notification, NotificationRequest request, User recipient) {
        notification.setRecipient(recipient);
        notification.setTitle(request.title());
        notification.setMessage(request.message());
        notification.setType(request.type());
        notification.setRead(request.read());
        notification.setReadAt(request.readAt());
    }

    public static NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getRecipient().getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }
}

