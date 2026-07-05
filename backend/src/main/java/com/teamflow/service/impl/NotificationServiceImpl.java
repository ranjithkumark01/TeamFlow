package com.teamflow.service.impl;

import com.teamflow.dto.request.NotificationRequest;
import com.teamflow.dto.response.NotificationResponse;
import com.teamflow.entity.Notification;
import com.teamflow.entity.User;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.NotificationMapper;
import com.teamflow.repository.NotificationRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.EmailNotificationService;
import com.teamflow.service.NotificationService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailNotificationService emailNotificationService;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(NotificationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> search(Long recipientId, Boolean read, Pageable pageable) {
        Specification<Notification> spec = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (recipientId != null) {
                predicates.add(cb.equal(root.get("recipient").get("id"), recipientId));
            }
            if (read != null) {
                predicates.add(cb.equal(root.get("read"), read));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return notificationRepository.findAll(spec, pageable).map(NotificationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse findById(Long id) {
        return NotificationMapper.toResponse(findNotification(id));
    }

    @Override
    public NotificationResponse create(NotificationRequest request) {
        User recipient = findUser(request.recipientId());
        Notification notification = notificationRepository.save(NotificationMapper.toEntity(request, recipient));
        emailNotificationService.send(recipient, notification.getTitle(), notification.getMessage());
        return NotificationMapper.toResponse(notification);
    }

    @Override
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification notification = findNotification(id);
        User recipient = findUser(request.recipientId());
        NotificationMapper.updateEntity(notification, request, recipient);
        return NotificationMapper.toResponse(notificationRepository.save(notification));
    }

    @Override
    public void delete(Long id) {
        notificationRepository.delete(findNotification(id));
    }

    private Notification findNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
