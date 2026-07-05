package com.teamflow.service.impl;

import com.teamflow.dto.request.TaskRelationRequest;
import com.teamflow.dto.response.TaskRelationResponse;
import com.teamflow.entity.Task;
import com.teamflow.entity.TaskRelation;
import com.teamflow.exception.BadRequestException;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.TaskRelationMapper;
import com.teamflow.repository.TaskRelationRepository;
import com.teamflow.repository.TaskRepository;
import com.teamflow.service.TaskRelationService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskRelationServiceImpl implements TaskRelationService {

    private final TaskRelationRepository taskRelationRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TaskRelationResponse> findAll(Pageable pageable) {
        return taskRelationRepository.findAll(pageable).map(TaskRelationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskRelationResponse findById(Long id) {
        return TaskRelationMapper.toResponse(findTaskRelation(id));
    }

    @Override
    public TaskRelationResponse create(TaskRelationRequest request) {
        validateNotSelfRelation(request);
        Task predecessor = findTask(request.predecessorTaskId());
        Task successor = findTask(request.successorTaskId());
        validateSameProject(predecessor, successor);
        validateNoCycle(predecessor.getId(), successor.getId(), null);
        return TaskRelationMapper.toResponse(taskRelationRepository.save(TaskRelationMapper.toEntity(request, predecessor, successor)));
    }

    @Override
    public TaskRelationResponse update(Long id, TaskRelationRequest request) {
        validateNotSelfRelation(request);
        TaskRelation relation = findTaskRelation(id);
        Task predecessor = findTask(request.predecessorTaskId());
        Task successor = findTask(request.successorTaskId());
        validateSameProject(predecessor, successor);
        validateNoCycle(predecessor.getId(), successor.getId(), id);
        TaskRelationMapper.updateEntity(relation, request, predecessor, successor);
        return TaskRelationMapper.toResponse(taskRelationRepository.save(relation));
    }

    @Override
    public void delete(Long id) {
        taskRelationRepository.delete(findTaskRelation(id));
    }

    private void validateNotSelfRelation(TaskRelationRequest request) {
        if (request.predecessorTaskId().equals(request.successorTaskId())) {
            throw new BadRequestException("A task relation cannot point to the same task.");
        }
    }

    private TaskRelation findTaskRelation(Long id) {
        return taskRelationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task relation", id));
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    private void validateSameProject(Task predecessor, Task successor) {
        if (!predecessor.getProject().getId().equals(successor.getProject().getId())) {
            throw new BadRequestException("Task dependencies must stay within the same project.");
        }
    }

    private void validateNoCycle(Long predecessorId, Long successorId, Long currentRelationId) {
        Map<Long, Set<Long>> graph = new HashMap<>();
        for (TaskRelation relation : taskRelationRepository.findAll()) {
            if (currentRelationId != null && currentRelationId.equals(relation.getId())) {
                continue;
            }
            graph.computeIfAbsent(relation.getPredecessorTask().getId(), ignored -> new HashSet<>())
                    .add(relation.getSuccessorTask().getId());
        }

        if (canReach(successorId, predecessorId, graph, new HashSet<>())) {
            throw new BadRequestException("This dependency would create a task cycle.");
        }
    }

    private boolean canReach(Long currentId, Long targetId, Map<Long, Set<Long>> graph, Set<Long> visited) {
        if (currentId.equals(targetId)) {
            return true;
        }
        if (!visited.add(currentId)) {
            return false;
        }
        for (Long nextId : graph.getOrDefault(currentId, Set.of())) {
            if (canReach(nextId, targetId, graph, visited)) {
                return true;
            }
        }
        return false;
    }
}
