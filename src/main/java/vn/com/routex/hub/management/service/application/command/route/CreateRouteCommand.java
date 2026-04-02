package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateRouteCommand(
        String creator,
        String pickupBranch,
        String origin,
        String destination,
        String plannedStartTime,
        String plannedEndTime,
        List<RoutePointCommand> routePoints,
        String requestId,
        String requestDateTime,
        String channel
) {
}
