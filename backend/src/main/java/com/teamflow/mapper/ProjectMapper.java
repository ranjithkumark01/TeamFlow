package com.teamflow.mapper;

import com.teamflow.dto.request.ProjectRequest;
import com.teamflow.dto.response.ProjectResponse;
import com.teamflow.entity.Project;
import com.teamflow.entity.User;

public final class ProjectMapper {

    private ProjectMapper() {
    }

    public static Project toEntity(ProjectRequest request, User createdBy) {
        Project project = new Project();
        updateEntity(project, request, createdBy);
        return project;
    }

    public static void updateEntity(Project project, ProjectRequest request, User createdBy) {
        project.setName(request.name());
        project.setDescription(request.description());
        project.setCreatedBy(createdBy);
    }

    public static ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedBy().getId(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}

