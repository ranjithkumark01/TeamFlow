package com.teamflow.repository;

import com.teamflow.entity.TaskRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRelationRepository extends JpaRepository<TaskRelation, Long> {
}

