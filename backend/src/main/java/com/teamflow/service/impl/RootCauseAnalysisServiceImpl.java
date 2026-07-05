package com.teamflow.service.impl;

import com.teamflow.dto.request.RootCauseAnalysisRequest;
import com.teamflow.dto.response.RootCauseAnalysisResponse;
import com.teamflow.entity.RootCauseAnalysis;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;
import com.teamflow.entity.enums.RcaStatus;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.RootCauseAnalysisMapper;
import com.teamflow.repository.RootCauseAnalysisRepository;
import com.teamflow.repository.TaskRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.RootCauseAnalysisService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RootCauseAnalysisServiceImpl implements RootCauseAnalysisService {

    private final RootCauseAnalysisRepository rootCauseAnalysisRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RootCauseAnalysisResponse> findAll(Pageable pageable) {
        return rootCauseAnalysisRepository.findAll(pageable).map(RootCauseAnalysisMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RootCauseAnalysisResponse> search(RcaStatus status, Long taskId, Pageable pageable) {
        Specification<RootCauseAnalysis> spec = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (taskId != null) {
                predicates.add(cb.equal(root.get("task").get("id"), taskId));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return rootCauseAnalysisRepository.findAll(spec, pageable).map(RootCauseAnalysisMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public RootCauseAnalysisResponse findById(Long id) {
        return RootCauseAnalysisMapper.toResponse(findRootCauseAnalysis(id));
    }

    @Override
    public RootCauseAnalysisResponse create(RootCauseAnalysisRequest request) {
        Task task = findTask(request.taskId());
        User createdBy = findUser(request.createdById());
        return RootCauseAnalysisMapper.toResponse(rootCauseAnalysisRepository.save(RootCauseAnalysisMapper.toEntity(request, task, createdBy)));
    }

    @Override
    public RootCauseAnalysisResponse update(Long id, RootCauseAnalysisRequest request) {
        RootCauseAnalysis analysis = findRootCauseAnalysis(id);
        Task task = findTask(request.taskId());
        User createdBy = findUser(request.createdById());
        RootCauseAnalysisMapper.updateEntity(analysis, request, task, createdBy);
        return RootCauseAnalysisMapper.toResponse(rootCauseAnalysisRepository.save(analysis));
    }

    @Override
    public void delete(Long id) {
        rootCauseAnalysisRepository.delete(findRootCauseAnalysis(id));
    }

    private RootCauseAnalysis findRootCauseAnalysis(Long id) {
        return rootCauseAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Root cause analysis", id));
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
