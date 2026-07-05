package com.teamflow.mapper;

import com.teamflow.dto.request.AttachmentRequest;
import com.teamflow.dto.response.AttachmentResponse;
import com.teamflow.entity.Attachment;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;

public final class AttachmentMapper {

    private AttachmentMapper() {
    }

    public static Attachment toEntity(AttachmentRequest request, Task task, User uploadedBy) {
        Attachment attachment = new Attachment();
        updateEntity(attachment, request, task, uploadedBy);
        return attachment;
    }

    public static void updateEntity(Attachment attachment, AttachmentRequest request, Task task, User uploadedBy) {
        attachment.setTask(task);
        attachment.setUploadedBy(uploadedBy);
        attachment.setFileName(request.fileName());
        attachment.setFileUrl(request.fileUrl());
        attachment.setContentType(request.contentType());
        attachment.setFileSize(request.fileSize());
    }

    public static AttachmentResponse toResponse(Attachment attachment) {
        return new AttachmentResponse(
                attachment.getId(),
                attachment.getTask().getId(),
                attachment.getUploadedBy().getId(),
                attachment.getFileName(),
                attachment.getFileUrl(),
                attachment.getContentType(),
                attachment.getFileSize(),
                attachment.getUploadedAt()
        );
    }
}

