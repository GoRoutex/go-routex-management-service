package vn.com.routex.hub.management.service.application.command.user;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.user.model.UserStatus;

@Builder
public record DeleteUserResult(
        String id,
        UserStatus status
) {
}
