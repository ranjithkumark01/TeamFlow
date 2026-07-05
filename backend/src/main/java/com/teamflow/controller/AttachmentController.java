package com.teamflow.controller;

import com.teamflow.api.ApiResponse;
import com.teamflow.api.PageResponse;
import com.teamflow.dto.request.AttachmentRequest;
import com.teamflow.dto.response.AttachmentResponse;
import com.teamflow.service.AttachmentService;
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
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AttachmentResponse>>> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Attachments retrieved", PageResponse.from(attachmentService.findAll(pageable))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttachmentResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Attachment retrieved", attachmentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttachmentResponse>> create(@Valid @RequestBody AttachmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Attachment created", attachmentService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttachmentResponse>> update(@PathVariable Long id, @Valid @RequestBody AttachmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Attachment updated", attachmentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attachmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

