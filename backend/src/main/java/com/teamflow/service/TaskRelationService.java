package com.teamflow.service;

import com.teamflow.dto.request.TaskRelationRequest;
import com.teamflow.dto.response.TaskRelationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRelationService {
    Page<TaskRelationResponse> findAll(Pageable pageable);
    TaskRelationResponse findById(Long id);
    TaskRelationResponse create(TaskRelationRequest request);
    TaskRelationResponse update(Long id, TaskRelationRequest request);
    void delete(Long id);
}

