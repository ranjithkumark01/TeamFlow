package com.teamflow.service.impl;

import com.teamflow.dto.request.RcaReviewRequest;
import com.teamflow.dto.response.RcaReviewResponse;
import com.teamflow.entity.RcaReview;
import com.teamflow.entity.RootCauseAnalysis;
import com.teamflow.entity.User;
import com.teamflow.entity.enums.RcaReviewDecision;
import com.teamflow.entity.enums.RcaStatus;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.RcaReviewMapper;
import com.teamflow.repository.RcaReviewRepository;
import com.teamflow.repository.RootCauseAnalysisRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.RcaReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RcaReviewServiceImpl implements RcaReviewService {

    private final RcaReviewRepository rcaReviewRepository;
    private final RootCauseAnalysisRepository rootCauseAnalysisRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RcaReviewResponse> findAll(Pageable pageable) {
        return rcaReviewRepository.findAll(pageable).map(RcaReviewMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public RcaReviewResponse findById(Long id) {
        return RcaReviewMapper.toResponse(findRcaReview(id));
    }

    @Override
    public RcaReviewResponse create(RcaReviewRequest request) {
        RootCauseAnalysis rca = findRootCauseAnalysis(request.rcaId());
        User reviewer = findUser(request.reviewerId());
        RcaReview review = rcaReviewRepository.save(RcaReviewMapper.toEntity(request, rca, reviewer));
        applyReviewDecision(rca, request.decision());
        return RcaReviewMapper.toResponse(review);
    }

    @Override
    public RcaReviewResponse update(Long id, RcaReviewRequest request) {
        RcaReview review = findRcaReview(id);
        RootCauseAnalysis rca = findRootCauseAnalysis(request.rcaId());
        User reviewer = findUser(request.reviewerId());
        RcaReviewMapper.updateEntity(review, request, rca, reviewer);
        RcaReview savedReview = rcaReviewRepository.save(review);
        applyReviewDecision(rca, request.decision());
        return RcaReviewMapper.toResponse(savedReview);
    }

    @Override
    public void delete(Long id) {
        rcaReviewRepository.delete(findRcaReview(id));
    }

    private RcaReview findRcaReview(Long id) {
        return rcaReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RCA review", id));
    }

    private RootCauseAnalysis findRootCauseAnalysis(Long id) {
        return rootCauseAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Root cause analysis", id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private void applyReviewDecision(RootCauseAnalysis rca, RcaReviewDecision decision) {
        if (decision == RcaReviewDecision.APPROVED) {
            rca.setStatus(RcaStatus.APPROVED);
        } else if (decision == RcaReviewDecision.REJECTED) {
            rca.setStatus(RcaStatus.REJECTED);
        } else if (decision == RcaReviewDecision.CHANGES_REQUESTED) {
            rca.setStatus(RcaStatus.IN_REVIEW);
        }
        rootCauseAnalysisRepository.save(rca);
    }
}
