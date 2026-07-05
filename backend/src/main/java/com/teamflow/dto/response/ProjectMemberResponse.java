package com.teamflow.dto.response;

import com.teamflow.entity.enums.ProjectMemberRole;
import java.time.OffsetDateTime;

public record ProjectMemberResponse(
        Long id,
        Long projectId,
        Long userId,
        ProjectMemberRole memberRole,
        OffsetDateTime joinedAt
) {
}

