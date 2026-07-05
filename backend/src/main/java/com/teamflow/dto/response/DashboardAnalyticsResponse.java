package com.teamflow.dto.response;

import java.util.Map;

public record DashboardAnalyticsResponse(
        long totalProjects,
        long totalTasks,
        long openTasks,
        long completedTasks,
        long blockedTasks,
        long totalUsers,
        long totalRootCauseAnalyses,
        Map<String, Long> tasksByStatus,
        Map<String, Long> tasksByPriority
) {
}

