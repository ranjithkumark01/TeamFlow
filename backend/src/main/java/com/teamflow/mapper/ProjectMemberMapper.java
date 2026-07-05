package com.teamflow.mapper;

import com.teamflow.dto.request.ProjectMemberRequest;
import com.teamflow.dto.response.ProjectMemberResponse;
import com.teamflow.entity.Project;
import com.teamflow.entity.ProjectMember;
import com.teamflow.entity.User;

public final class ProjectMemberMapper {

    private ProjectMemberMapper() {
    }

    public static ProjectMember toEntity(ProjectMemberRequest request, Project project, User user) {
        ProjectMember member = new ProjectMember();
        updateEntity(member, request, project, user);
        return member;
    }

    public static void updateEntity(ProjectMember member, ProjectMemberRequest request, Project project, User user) {
        member.setProject(project);
        member.setUser(user);
        member.setMemberRole(request.memberRole());
    }

    public static ProjectMemberResponse toResponse(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId(),
                member.getProject().getId(),
                member.getUser().getId(),
                member.getMemberRole(),
                member.getJoinedAt()
        );
    }
}

