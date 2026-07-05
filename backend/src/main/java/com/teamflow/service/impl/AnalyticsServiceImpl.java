package com.teamflow.service.impl;

import com.teamflow.dto.response.DashboardAnalyticsResponse;
import com.teamflow.entity.Task;
import com.teamflow.entity.enums.TaskPriority;
import com.teamflow.entity.enums.TaskStatus;
import com.teamflow.repository.ProjectRepository;
import com.teamflow.repository.RootCauseAnalysisRepository;
import com.teamflow.repository.TaskRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.AnalyticsService;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RootCauseAnalysisRepository rootCauseAnalysisRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardAnalyticsResponse getDashboardAnalytics() {
        var tasks = taskRepository.findAll();
        Map<String, Long> byStatus = Arrays.stream(TaskStatus.values())
                .collect(Collectors.toMap(Enum::name, status -> tasks.stream().filter(task -> task.getStatus() == status).count()));
        Map<String, Long> byPriority = Arrays.stream(TaskPriority.values())
                .collect(Collectors.toMap(Enum::name, priority -> tasks.stream().filter(task -> task.getPriority() == priority).count()));

        long openTasks = tasks.stream().filter(this::isOpen).count();
        long completedTasks = byStatus.getOrDefault(TaskStatus.DONE.name(), 0L);
        long blockedTasks = byStatus.getOrDefault(TaskStatus.BLOCKED.name(), 0L);

        return new DashboardAnalyticsResponse(
                projectRepository.count(),
                tasks.size(),
                openTasks,
                completedTasks,
                blockedTasks,
                userRepository.count(),
                rootCauseAnalysisRepository.count(),
                byStatus,
                byPriority
        );
    }

    private boolean isOpen(Task task) {
        return task.getStatus() != TaskStatus.DONE && task.getStatus() != TaskStatus.CANCELLED;
    }
}

