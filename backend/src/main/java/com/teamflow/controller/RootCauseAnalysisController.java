package com.teamflow.controller;

import com.teamflow.api.ApiResponse;
import com.teamflow.api.PageResponse;
import com.teamflow.dto.request.RootCauseAnalysisRequest;
import com.teamflow.dto.response.RootCauseAnalysisResponse;
import com.teamflow.entity.enums.RcaStatus;
import com.teamflow.service.RootCauseAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/root-cause-analyses")
@RequiredArgsConstructor
public class RootCauseAnalysisController {

    private final RootCauseAnalysisService rootCauseAnalysisService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RootCauseAnalysisResponse>>> findAll(
            @RequestParam(required = false) RcaStatus status,
            @RequestParam(required = false) Long taskId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success("Root cause analyses retrieved", PageResponse.from(rootCauseAnalysisService.search(status, taskId, pageable))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RootCauseAnalysisResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Root cause analysis retrieved", rootCauseAnalysisService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RootCauseAnalysisResponse>> create(@Valid @RequestBody RootCauseAnalysisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Root cause analysis created", rootCauseAnalysisService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RootCauseAnalysisResponse>> update(@PathVariable Long id, @Valid @RequestBody RootCauseAnalysisRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Root cause analysis updated", rootCauseAnalysisService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rootCauseAnalysisService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
