package com.teamflow.dto.response;

import com.teamflow.entity.enums.RcaStatus;
import java.time.OffsetDateTime;

public record RootCauseAnalysisResponse(
        Long id,
        Long taskId,
        Long createdById,
        String problemSummary,
        String rootCause,
        String correctiveAction,
        String preventiveAction,
        RcaStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

