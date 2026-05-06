package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.trip.TripStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record FetchTripResult(
        String id,
        String creator,
        String pickupBranch,
        String tripCode,
        String originCode,
        String originName,
        String destinationCode,
        String destinationName,
        String originProvinceId,
        String destinationProvinceId,
        String originDepartmentId,
        String destinationDepartmentId,
        OffsetDateTime departureTime,
        String rawDepartureDate,
        String rawDepartureTime,
        Long durationMinutes,
        TripStatus status,
        String vehicleId,
        String vehiclePlate,
        Boolean hasFloor,
        OffsetDateTime assignedAt,
        List<RoutePointResult> routePoints
) {
}
