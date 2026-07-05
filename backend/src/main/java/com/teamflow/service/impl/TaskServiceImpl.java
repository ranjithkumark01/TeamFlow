package com.teamflow.service.impl;

import com.teamflow.dto.request.TaskRequest;
import com.teamflow.dto.request.NotificationRequest;
import com.teamflow.dto.response.TaskResponse;
import com.teamflow.entity.Project;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;
import com.teamflow.entity.enums.NotificationType;
import com.teamflow.entity.enums.TaskPriority;
import com.teamflow.entity.enums.TaskStatus;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.TaskMapper;
import com.teamflow.repository.ProjectRepository;
import com.teamflow.repository.TaskRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.NotificationService;
import com.teamflow.service.TaskService;
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
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable).map(TaskMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> search(String query, TaskStatus status, TaskPriority priority, Long projectId, Long assigneeId, Pageable pageable) {
        Specification<Task> spec = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query != null && !query.isBlank()) {
                String like = "%" + query.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like)
                ));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            if (projectId != null) {
                predicates.add(cb.equal(root.get("project").get("id"), projectId));
            }
            if (assigneeId != null) {
                predicates.add(cb.equal(root.get("assignee").get("id"), assigneeId));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return taskRepository.findAll(spec, pageable).map(TaskMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse findById(Long id) {
        return TaskMapper.toResponse(findTask(id));
    }

    @Override
    public TaskResponse create(TaskRequest request) {
        Project project = findProject(request.projectId());
        User assignee = request.assigneeId() == null ? null : findUser(request.assigneeId());
        Task savedTask = taskRepository.save(TaskMapper.toEntity(request, project, assignee));
        notifyAssignee(savedTask, "Task assigned", "You were assigned to task: " + savedTask.getTitle());
        return TaskMapper.toResponse(savedTask);
    }

    @Override
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findTask(id);
        Project project = findProject(request.projectId());
        User assignee = request.assigneeId() == null ? null : findUser(request.assigneeId());
        TaskMapper.updateEntity(task, request, project, assignee);
        Task savedTask = taskRepository.save(task);
        notifyAssignee(savedTask, "Task updated", "Task updated: " + savedTask.getTitle());
        return TaskMapper.toResponse(savedTask);
    }

    @Override
    public void delete(Long id) {
        taskRepository.delete(findTask(id));
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private void notifyAssignee(Task task, String title, String message) {
        if (task.getAssignee() == null) {
            return;
        }
        notificationService.create(new NotificationRequest(
                task.getAssignee().getId(),
                title,
                message,
                NotificationType.TASK_ASSIGNED,
                false,
                null
        ));
    }
}
