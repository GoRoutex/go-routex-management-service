package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record SearchRouteItemResult(
        String id,
        String merchantId,
        String vehicleId,
        String driverId,
        String merchantName,
        String pickupBranch,
        String origin,
        String destination,
        Long availableSeats,
        BigDecimal ticketPrice,
        OffsetDateTime plannedStartTime,
        OffsetDateTime plannedEndTime,
        String vehiclePlate,
        boolean hasFloor,
        String routeCode,
        List<RoutePointResult> routePoints
) {
}
