package com.teamflow.controller;

import com.teamflow.api.ApiResponse;
import com.teamflow.dto.request.AttachmentRequest;
import com.teamflow.dto.response.AttachmentResponse;
import com.teamflow.exception.BadRequestException;
import com.teamflow.service.AttachmentService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final AttachmentService attachmentService;

    @Value("${teamflow.upload-dir:uploads}")
    private String uploadDir;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AttachmentResponse>> upload(
            @RequestParam Long taskId,
            @RequestParam Long uploadedById,
            @RequestParam MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file cannot be empty.");
        }

        Path storageDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(storageDir);

        String originalName = Paths.get(file.getOriginalFilename() == null ? "upload.bin" : file.getOriginalFilename()).getFileName().toString();
        String storedName = UUID.randomUUID() + "-" + originalName;
        Path destination = storageDir.resolve(storedName).normalize();
        if (!destination.startsWith(storageDir)) {
            throw new BadRequestException("Invalid upload path.");
        }
        file.transferTo(destination);

        AttachmentResponse response = attachmentService.create(new AttachmentRequest(
                taskId,
                uploadedById,
                originalName,
                "/api/files/" + storedName,
                file.getContentType(),
                file.getSize()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("File uploaded", response));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName) throws MalformedURLException {
        Path storageDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = storageDir.resolve(fileName).normalize();
        if (!filePath.startsWith(storageDir) || !Files.exists(filePath)) {
            throw new BadRequestException("File not found.");
        }

        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

