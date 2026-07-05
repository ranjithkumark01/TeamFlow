package com.teamflow.dto.request;

import com.teamflow.entity.enums.ProjectMemberRole;
import jakarta.validation.constraints.NotNull;

public record ProjectMemberRequest(
        @NotNull Long projectId,
        @NotNull Long userId,
        @NotNull ProjectMemberRole memberRole
) {
}

