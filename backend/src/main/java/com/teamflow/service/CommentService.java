package com.teamflow.service;

import com.teamflow.dto.request.CommentRequest;
import com.teamflow.dto.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Page<CommentResponse> findAll(Pageable pageable);
    CommentResponse findById(Long id);
    CommentResponse create(CommentRequest request);
    CommentResponse update(Long id, CommentRequest request);
    void delete(Long id);
}

