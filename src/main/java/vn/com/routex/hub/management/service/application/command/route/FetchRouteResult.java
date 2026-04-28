package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record FetchRouteResult(
        String id,
        String creator,
        String pickupBranch,
        String routeCode,
        String origin,
        String destination,
        OffsetDateTime plannedStartTime,
        OffsetDateTime plannedEndTime,
        OffsetDateTime actualStartTime,
        OffsetDateTime actualEndTime,
        String status,
        String vehicleId,
        String vehiclePlate,
        Boolean hasFloor,
        OffsetDateTime assignedAt,
        List<RoutePointResult> routePoints
) {
}
