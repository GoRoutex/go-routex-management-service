package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record AssignRouteResult(
        String creator,
        String routeId,
        String vehicleId,
        String driverId,
        String assignedAt,
        String status
) {
}
