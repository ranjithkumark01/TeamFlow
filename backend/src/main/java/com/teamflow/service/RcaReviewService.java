package com.teamflow.service;

import com.teamflow.dto.request.RcaReviewRequest;
import com.teamflow.dto.response.RcaReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RcaReviewService {
    Page<RcaReviewResponse> findAll(Pageable pageable);
    RcaReviewResponse findById(Long id);
    RcaReviewResponse create(RcaReviewRequest request);
    RcaReviewResponse update(Long id, RcaReviewRequest request);
    void delete(Long id);
}

