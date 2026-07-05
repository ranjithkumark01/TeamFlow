package com.teamflow.service;

import com.teamflow.dto.request.ProjectMemberRequest;
import com.teamflow.dto.response.ProjectMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectMemberService {
    Page<ProjectMemberResponse> findAll(Pageable pageable);
    ProjectMemberResponse findById(Long id);
    ProjectMemberResponse create(ProjectMemberRequest request);
    ProjectMemberResponse update(Long id, ProjectMemberRequest request);
    void delete(Long id);
}

