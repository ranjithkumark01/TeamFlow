package com.teamflow.dto.request;

import com.teamflow.entity.enums.RcaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RootCauseAnalysisRequest(
        @NotNull Long taskId,
        @NotNull Long createdById,
        @NotBlank String problemSummary,
        String rootCause,
        String correctiveAction,
        String preventiveAction,
        @NotNull RcaStatus status
) {
}

