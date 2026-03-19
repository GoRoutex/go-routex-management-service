package vn.com.routex.hub.management.service.infrastructure.kafka.event;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record RouteSellableEvent(
        String routeId,
        String vehicleId,
        String assignedBy,
        OffsetDateTime assignedAt,
        String routeStatus,
        Integer seatCount,
        String creator,
        Boolean hasFloor
) {
}
