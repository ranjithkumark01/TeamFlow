package com.teamflow.dto.response;

import com.teamflow.entity.enums.TaskRelationType;
import java.time.OffsetDateTime;

public record TaskRelationResponse(
        Long id,
        Long predecessorTaskId,
        Long successorTaskId,
        TaskRelationType relationType,
        OffsetDateTime createdAt
) {
}

