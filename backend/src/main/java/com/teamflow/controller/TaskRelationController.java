package com.teamflow.controller;

import com.teamflow.api.ApiResponse;
import com.teamflow.api.PageResponse;
import com.teamflow.dto.request.TaskRelationRequest;
import com.teamflow.dto.response.TaskRelationResponse;
import com.teamflow.service.TaskRelationService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task-relations")
@RequiredArgsConstructor
public class TaskRelationController {

    private final TaskRelationService taskRelationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskRelationResponse>>> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Task relations retrieved", PageResponse.from(taskRelationService.findAll(pageable))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskRelationResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Task relation retrieved", taskRelationService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskRelationResponse>> create(@Valid @RequestBody TaskRelationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task relation created", taskRelationService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskRelationResponse>> update(@PathVariable Long id, @Valid @RequestBody TaskRelationRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Task relation updated", taskRelationService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskRelationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

