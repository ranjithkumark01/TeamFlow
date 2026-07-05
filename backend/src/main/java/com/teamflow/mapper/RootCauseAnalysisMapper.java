package com.teamflow.mapper;

import com.teamflow.dto.request.RootCauseAnalysisRequest;
import com.teamflow.dto.response.RootCauseAnalysisResponse;
import com.teamflow.entity.RootCauseAnalysis;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;

public final class RootCauseAnalysisMapper {

    private RootCauseAnalysisMapper() {
    }

    public static RootCauseAnalysis toEntity(RootCauseAnalysisRequest request, Task task, User createdBy) {
        RootCauseAnalysis analysis = new RootCauseAnalysis();
        updateEntity(analysis, request, task, createdBy);
        return analysis;
    }

    public static void updateEntity(RootCauseAnalysis analysis, RootCauseAnalysisRequest request, Task task, User createdBy) {
        analysis.setTask(task);
        analysis.setCreatedBy(createdBy);
        analysis.setProblemSummary(request.problemSummary());
        analysis.setRootCause(request.rootCause());
        analysis.setCorrectiveAction(request.correctiveAction());
        analysis.setPreventiveAction(request.preventiveAction());
        analysis.setStatus(request.status());
    }

    public static RootCauseAnalysisResponse toResponse(RootCauseAnalysis analysis) {
        return new RootCauseAnalysisResponse(
                analysis.getId(),
                analysis.getTask().getId(),
                analysis.getCreatedBy().getId(),
                analysis.getProblemSummary(),
                analysis.getRootCause(),
                analysis.getCorrectiveAction(),
                analysis.getPreventiveAction(),
                analysis.getStatus(),
                analysis.getCreatedAt(),
                analysis.getUpdatedAt()
        );
    }
}

