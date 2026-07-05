package com.teamflow.service.impl;

import com.teamflow.dto.request.AttachmentRequest;
import com.teamflow.dto.response.AttachmentResponse;
import com.teamflow.entity.Attachment;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.AttachmentMapper;
import com.teamflow.repository.AttachmentRepository;
import com.teamflow.repository.TaskRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentResponse> findAll(Pageable pageable) {
        return attachmentRepository.findAll(pageable).map(AttachmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentResponse findById(Long id) {
        return AttachmentMapper.toResponse(findAttachment(id));
    }

    @Override
    public AttachmentResponse create(AttachmentRequest request) {
        Task task = findTask(request.taskId());
        User uploadedBy = findUser(request.uploadedById());
        return AttachmentMapper.toResponse(attachmentRepository.save(AttachmentMapper.toEntity(request, task, uploadedBy)));
    }

    @Override
    public AttachmentResponse update(Long id, AttachmentRequest request) {
        Attachment attachment = findAttachment(id);
        Task task = findTask(request.taskId());
        User uploadedBy = findUser(request.uploadedById());
        AttachmentMapper.updateEntity(attachment, request, task, uploadedBy);
        return AttachmentMapper.toResponse(attachmentRepository.save(attachment));
    }

    @Override
    public void delete(Long id) {
        attachmentRepository.delete(findAttachment(id));
    }

    private Attachment findAttachment(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment", id));
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}

