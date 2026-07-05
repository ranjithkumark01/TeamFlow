package com.teamflow.dto.response;

import com.teamflow.entity.enums.RcaReviewDecision;
import java.time.OffsetDateTime;

public record RcaReviewResponse(
        Long id,
        Long rcaId,
        Long reviewerId,
        RcaReviewDecision decision,
        String comments,
        OffsetDateTime reviewedAt
) {
}

