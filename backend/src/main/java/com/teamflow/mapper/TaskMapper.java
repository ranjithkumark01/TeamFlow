package com.teamflow.mapper;

import com.teamflow.dto.request.TaskRequest;
import com.teamflow.dto.response.TaskResponse;
import com.teamflow.entity.Project;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(TaskRequest request, Project project, User assignee) {
        Task task = new Task();
        updateEntity(task, request, project, assignee);
        return task;
    }

    public static void updateEntity(Task task, TaskRequest request, Project project, User assignee) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());
        task.setAssignee(assignee);
        task.setProject(project);
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getAssignee() == null ? null : task.getAssignee().getId(),
                task.getProject().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}

