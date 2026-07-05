package com.teamflow.service;

import com.teamflow.dto.request.RootCauseAnalysisRequest;
import com.teamflow.dto.response.RootCauseAnalysisResponse;
import com.teamflow.entity.enums.RcaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RootCauseAnalysisService {
    Page<RootCauseAnalysisResponse> findAll(Pageable pageable);
    Page<RootCauseAnalysisResponse> search(RcaStatus status, Long taskId, Pageable pageable);
    RootCauseAnalysisResponse findById(Long id);
    RootCauseAnalysisResponse create(RootCauseAnalysisRequest request);
    RootCauseAnalysisResponse update(Long id, RootCauseAnalysisRequest request);
    void delete(Long id);
}
