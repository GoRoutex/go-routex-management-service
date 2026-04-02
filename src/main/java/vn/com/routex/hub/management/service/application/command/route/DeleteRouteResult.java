package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record DeleteRouteResult(
        String creator,
        String routeId,
        String routeCode,
        String status,
        OffsetDateTime updatedAt
) {
}
