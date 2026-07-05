package com.teamflow.service.impl;

import com.teamflow.dto.request.ProjectRequest;
import com.teamflow.dto.response.ProjectResponse;
import com.teamflow.entity.Project;
import com.teamflow.entity.User;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.ProjectMapper;
import com.teamflow.repository.ProjectRepository;
import com.teamflow.repository.UserRepository;
import com.teamflow.service.ProjectService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable).map(ProjectMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> search(String query, Long createdById, Pageable pageable) {
        Specification<Project> spec = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query != null && !query.isBlank()) {
                String like = "%" + query.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("description")), like)
                ));
            }
            if (createdById != null) {
                predicates.add(cb.equal(root.get("createdBy").get("id"), createdById));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return projectRepository.findAll(spec, pageable).map(ProjectMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id) {
        return ProjectMapper.toResponse(findProject(id));
    }

    @Override
    public ProjectResponse create(ProjectRequest request) {
        User createdBy = findUser(request.createdById());
        return ProjectMapper.toResponse(projectRepository.save(ProjectMapper.toEntity(request, createdBy)));
    }

    @Override
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = findProject(id);
        User createdBy = findUser(request.createdById());
        ProjectMapper.updateEntity(project, request, createdBy);
        return ProjectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public void delete(Long id) {
        projectRepository.delete(findProject(id));
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
