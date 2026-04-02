package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record DeleteRouteCommand(
        String creator,
        String routeId,
        String requestId,
        String requestDateTime,
        String channel
) {
}
