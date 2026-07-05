package com.teamflow.service;

import com.teamflow.dto.request.NotificationRequest;
import com.teamflow.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationResponse> findAll(Pageable pageable);
    Page<NotificationResponse> search(Long recipientId, Boolean read, Pageable pageable);
    NotificationResponse findById(Long id);
    NotificationResponse create(NotificationRequest request);
    NotificationResponse update(Long id, NotificationRequest request);
    void delete(Long id);
}
