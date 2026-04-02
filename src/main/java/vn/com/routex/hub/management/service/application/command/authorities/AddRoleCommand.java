package vn.com.routex.hub.management.service.application.command.authorities;

import lombok.Builder;

@Builder
public record AddRoleCommand(
        String code,
        String name,
        String description,
        String creator,
        boolean enabled,
        String requestId,
        String requestDateTime,
        String channel
) {
}
