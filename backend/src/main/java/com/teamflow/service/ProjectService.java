package com.teamflow.service;

import com.teamflow.dto.request.ProjectRequest;
import com.teamflow.dto.response.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    Page<ProjectResponse> findAll(Pageable pageable);
    Page<ProjectResponse> search(String query, Long createdById, Pageable pageable);
    ProjectResponse findById(Long id);
    ProjectResponse create(ProjectRequest request);
    ProjectResponse update(Long id, ProjectRequest request);
    void delete(Long id);
}
