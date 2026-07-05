package com.teamflow.mapper;

import com.teamflow.dto.request.CommentRequest;
import com.teamflow.dto.response.CommentResponse;
import com.teamflow.entity.Comment;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toEntity(CommentRequest request, Task task, User author) {
        Comment comment = new Comment();
        updateEntity(comment, request, task, author);
        return comment;
    }

    public static void updateEntity(Comment comment, CommentRequest request, Task task, User author) {
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setContent(request.content());
    }

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getAuthor().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}

