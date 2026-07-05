package com.teamflow.service;

import com.teamflow.dto.request.AttachmentRequest;
import com.teamflow.dto.response.AttachmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttachmentService {
    Page<AttachmentResponse> findAll(Pageable pageable);
    AttachmentResponse findById(Long id);
    AttachmentResponse create(AttachmentRequest request);
    AttachmentResponse update(Long id, AttachmentRequest request);
    void delete(Long id);
}

