package vn.com.routex.hub.management.service.application.command.user;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;
import vn.com.routex.hub.management.service.domain.user.model.Gender;
import vn.com.routex.hub.management.service.domain.user.model.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Builder
public record UpdateUserCommand(
        RequestContext context,
        String userId,
        String email,
        String phoneNumber,
        String avatarUrl,
        String address,
        LocalDate dob,
        Gender gender,
        String nationalId,
        Boolean phoneVerified,
        Boolean profileCompleted,
        Boolean emailVerified,
        UserStatus status,
        String tenantId,
        String language,
        String timezone,
        Integer failLoginCount,
        OffsetDateTime lastLoginAt,
        OffsetDateTime lockedUntil,
        String updatedBy
) {
}
