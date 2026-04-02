package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record FetchRouteQuery(
        String routeId,
        String requestId,
        String requestDateTime,
        String channel
) {
}
