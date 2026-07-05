package com.teamflow.service;

import com.teamflow.dto.request.TaskRequest;
import com.teamflow.dto.response.TaskResponse;
import com.teamflow.entity.enums.TaskPriority;
import com.teamflow.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Page<TaskResponse> findAll(Pageable pageable);
    Page<TaskResponse> search(String query, TaskStatus status, TaskPriority priority, Long projectId, Long assigneeId, Pageable pageable);
    TaskResponse findById(Long id);
    TaskResponse create(TaskRequest request);
    TaskResponse update(Long id, TaskRequest request);
    void delete(Long id);
}
