package com.teamflow.dto.request;

import com.teamflow.entity.enums.RcaReviewDecision;
import jakarta.validation.constraints.NotNull;

public record RcaReviewRequest(
        @NotNull Long rcaId,
        @NotNull Long reviewerId,
        @NotNull RcaReviewDecision decision,
        String comments
) {
}

