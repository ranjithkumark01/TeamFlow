package com.teamflow.service;

import com.teamflow.entity.User;

public interface EmailNotificationService {
    void send(User recipient, String subject, String message);
}

