package com.teamflow.repository;

import com.teamflow.entity.RootCauseAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RootCauseAnalysisRepository extends JpaRepository<RootCauseAnalysis, Long>, JpaSpecificationExecutor<RootCauseAnalysis> {
}
