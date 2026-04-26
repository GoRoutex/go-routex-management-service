package vn.com.routex.hub.management.service.infrastructure.kafka.event;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;

import java.time.OffsetDateTime;

@Builder
public record RouteAssignedEvent(
        String routeId,
        String vehicleId,
        String driverId,
        OffsetDateTime departureTime,
        String originName,
        String destinationName,
        String assignedBy,
        OffsetDateTime assignedAt,
        RouteStatus status
) {
}
