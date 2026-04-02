package vn.com.routex.hub.management.service.application.command.authorities;

import lombok.Builder;

@Builder
public record SetRoleCommand(
        String userId,
        String roleId,
        String requestId,
        String requestDateTime,
        String channel
) {
}
