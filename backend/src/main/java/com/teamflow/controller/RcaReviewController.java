package com.teamflow.controller;

import com.teamflow.api.ApiResponse;
import com.teamflow.api.PageResponse;
import com.teamflow.dto.request.RcaReviewRequest;
import com.teamflow.dto.response.RcaReviewResponse;
import com.teamflow.service.RcaReviewService;
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
@RequestMapping("/api/rca-reviews")
@RequiredArgsConstructor
public class RcaReviewController {

    private final RcaReviewService rcaReviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RcaReviewResponse>>> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("RCA reviews retrieved", PageResponse.from(rcaReviewService.findAll(pageable))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RcaReviewResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("RCA review retrieved", rcaReviewService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RcaReviewResponse>> create(@Valid @RequestBody RcaReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("RCA review created", rcaReviewService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RcaReviewResponse>> update(@PathVariable Long id, @Valid @RequestBody RcaReviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success("RCA review updated", rcaReviewService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rcaReviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

