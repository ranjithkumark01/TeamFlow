package com.teamflow.service.impl;

import com.teamflow.dto.request.ProjectMemberRequest;
import com.teamflow.dto.response.ProjectMemberResponse;
import com.teamflow.entity.Project;
import com.teamflow.entity.ProjectMember;
import com.teamflow.entity.User;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.ProjectMemberMapper;
import com.teamflow.repository.ProjectMemberRepository;
import com.teamflow.repository.ProjectRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMemberResponse> findAll(Pageable pageable) {
        return projectMemberRepository.findAll(pageable).map(ProjectMemberMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectMemberResponse findById(Long id) {
        return ProjectMemberMapper.toResponse(findProjectMember(id));
    }

    @Override
    public ProjectMemberResponse create(ProjectMemberRequest request) {
        Project project = findProject(request.projectId());
        User user = findUser(request.userId());
        return ProjectMemberMapper.toResponse(projectMemberRepository.save(ProjectMemberMapper.toEntity(request, project, user)));
    }

    @Override
    public ProjectMemberResponse update(Long id, ProjectMemberRequest request) {
        ProjectMember member = findProjectMember(id);
        Project project = findProject(request.projectId());
        User user = findUser(request.userId());
        ProjectMemberMapper.updateEntity(member, request, project, user);
        return ProjectMemberMapper.toResponse(projectMemberRepository.save(member));
    }

    @Override
    public void delete(Long id) {
        projectMemberRepository.delete(findProjectMember(id));
    }

    private ProjectMember findProjectMember(Long id) {
        return projectMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project member", id));
    }

    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}

