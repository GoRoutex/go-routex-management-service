package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record SearchTripItemResult(
        String id,
        String merchantId,
        String vehicleId,
        String driverId,
        String merchantName,
        String pickupBranch,
        String originCode,
        String originName,
        String destinationCode,
        String destinationName,
        Long availableSeats,
        BigDecimal ticketPrice,
        OffsetDateTime departureTime,
        String rawDepartureDate,
        String rawDepartureTime,
        String vehiclePlate,
        boolean hasFloor,
        String tripCode,
        List<RoutePointResult> routePoints
) {
}
