package com.teamflow.mapper;

import com.teamflow.dto.request.TaskRelationRequest;
import com.teamflow.dto.response.TaskRelationResponse;
import com.teamflow.entity.Task;
import com.teamflow.entity.TaskRelation;

public final class TaskRelationMapper {

    private TaskRelationMapper() {
    }

    public static TaskRelation toEntity(TaskRelationRequest request, Task predecessorTask, Task successorTask) {
        TaskRelation relation = new TaskRelation();
        updateEntity(relation, request, predecessorTask, successorTask);
        return relation;
    }

    public static void updateEntity(TaskRelation relation, TaskRelationRequest request, Task predecessorTask, Task successorTask) {
        relation.setPredecessorTask(predecessorTask);
        relation.setSuccessorTask(successorTask);
        relation.setRelationType(request.relationType());
    }

    public static TaskRelationResponse toResponse(TaskRelation relation) {
        return new TaskRelationResponse(
                relation.getId(),
                relation.getPredecessorTask().getId(),
                relation.getSuccessorTask().getId(),
                relation.getRelationType(),
                relation.getCreatedAt()
        );
    }
}

