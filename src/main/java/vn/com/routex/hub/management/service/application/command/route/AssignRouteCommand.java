package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record AssignRouteCommand(
        String creator,
        String routeId,
        String vehicleId,
        String driverId,
        String requestId,
        String requestDateTime,
        String channel
) {
}
