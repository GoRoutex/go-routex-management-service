package vn.com.routex.hub.management.service.application.command.authorities;

import lombok.Builder;

@Builder
public record AddPermissionResult(
        String code,
        String name,
        String creator,
        String description
) {
}
