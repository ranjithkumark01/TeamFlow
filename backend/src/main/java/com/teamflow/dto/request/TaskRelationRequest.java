package com.teamflow.dto.request;

import com.teamflow.entity.enums.TaskRelationType;
import jakarta.validation.constraints.NotNull;

public record TaskRelationRequest(
        @NotNull Long predecessorTaskId,
        @NotNull Long successorTaskId,
        @NotNull TaskRelationType relationType
) {
}

