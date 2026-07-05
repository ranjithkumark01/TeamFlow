package com.teamflow.service.impl;

import com.teamflow.dto.request.CommentRequest;
import com.teamflow.dto.response.CommentResponse;
import com.teamflow.entity.Comment;
import com.teamflow.entity.Task;
import com.teamflow.entity.User;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.CommentMapper;
import com.teamflow.repository.CommentRepository;
import com.teamflow.repository.TaskRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable).map(CommentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponse findById(Long id) {
        return CommentMapper.toResponse(findComment(id));
    }

    @Override
    public CommentResponse create(CommentRequest request) {
        Task task = findTask(request.taskId());
        User author = findUser(request.authorId());
        return CommentMapper.toResponse(commentRepository.save(CommentMapper.toEntity(request, task, author)));
    }

    @Override
    public CommentResponse update(Long id, CommentRequest request) {
        Comment comment = findComment(id);
        Task task = findTask(request.taskId());
        User author = findUser(request.authorId());
        CommentMapper.updateEntity(comment, request, task, author);
        return CommentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) {
        commentRepository.delete(findComment(id));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", id));
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}

