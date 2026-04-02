package vn.com.routex.hub.management.service.application.command.route;


import lombok.Builder;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record UpdateRouteResult(
        String routeId,
        String creator,
        String pickupBranch,
        String origin,
        String destination,
        OffsetDateTime plannedStartTime,
        OffsetDateTime plannedEndTime,
        OffsetDateTime actualStartTime,
        OffsetDateTime actualEndTime,
        RouteStatus status,
        List<UpdateRoutePointResult> routePoints
) {
    @Builder
    public record UpdateRoutePointResult(
            String id,
            String operationOrder,
            OffsetDateTime plannedArrivalTime,
            OffsetDateTime plannedDepartureTime,
            String note
    ) {

    }
}
