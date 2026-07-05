package com.teamflow.mapper;

import com.teamflow.dto.request.RcaReviewRequest;
import com.teamflow.dto.response.RcaReviewResponse;
import com.teamflow.entity.RcaReview;
import com.teamflow.entity.RootCauseAnalysis;
import com.teamflow.entity.User;

public final class RcaReviewMapper {

    private RcaReviewMapper() {
    }

    public static RcaReview toEntity(RcaReviewRequest request, RootCauseAnalysis rca, User reviewer) {
        RcaReview review = new RcaReview();
        updateEntity(review, request, rca, reviewer);
        return review;
    }

    public static void updateEntity(RcaReview review, RcaReviewRequest request, RootCauseAnalysis rca, User reviewer) {
        review.setRca(rca);
        review.setReviewer(reviewer);
        review.setDecision(request.decision());
        review.setComments(request.comments());
    }

    public static RcaReviewResponse toResponse(RcaReview review) {
        return new RcaReviewResponse(
                review.getId(),
                review.getRca().getId(),
                review.getReviewer().getId(),
                review.getDecision(),
                review.getComments(),
                review.getReviewedAt()
        );
    }
}

