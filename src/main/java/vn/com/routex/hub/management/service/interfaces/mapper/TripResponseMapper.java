package vn.com.routex.hub.management.service.interfaces.mapper;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.command.route.FetchTripResult;
import vn.com.routex.hub.management.service.application.command.route.RoutePointResult;
import vn.com.routex.hub.management.service.application.command.route.SearchTripItemResult;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchTripResponse;
import vn.com.routex.hub.management.service.interfaces.models.trip.FetchTripResponse;

@Component
public class TripResponseMapper {

    public FetchTripResponse.FetchTripResponseData toFetchRouteResponseData(FetchTripResult item) {
        return FetchTripResponse.FetchTripResponseData.builder()
                .id(item.id())
                .creator(item.creator())
                .pickupBranch(item.pickupBranch())
                .tripCode(item.tripCode())
                .originCode(item.originCode())
                .originName(item.originName())
                .destinationCode(item.destinationCode())
                .destinationName(item.destinationName())
                .originProvinceId(item.originProvinceId())
                .destinationProvinceId(item.destinationProvinceId())
                .originDepartmentId(item.originDepartmentId())
                .destinationDepartmentId(item.destinationDepartmentId())
                .departureTime(item.departureTime())
                .rawDepartureDate(item.rawDepartureDate())
                .rawDepartureTime(item.rawDepartureTime())
                .rawArrivalTime(calculateArrivalTime(item.rawDepartureTime(), item.durationMinutes()))
                .status(item.status())
                .vehicleId(item.vehicleId())
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .assignedAt(item.assignedAt())
                .routePoints(item.routePoints() == null ? null : item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }

    public FetchTripResponse.FetchTripResponseData toPublicFetchTripResponseData(FetchTripResult item) {
        return FetchTripResponse.FetchTripResponseData.builder()
                .id(item.id())
                .tripCode(item.tripCode())
                .originCode(item.originCode())
                .originName(item.originName())
                .destinationCode(item.destinationCode())
                .destinationName(item.destinationName())
                .originProvinceId(item.originProvinceId())
                .destinationProvinceId(item.destinationProvinceId())
                .originDepartmentId(item.originDepartmentId())
                .destinationDepartmentId(item.destinationDepartmentId())
                .rawDepartureTime(item.rawDepartureTime())
                .rawDepartureDate(item.rawDepartureDate())
                .rawArrivalTime(calculateArrivalTime(item.rawDepartureTime(), item.durationMinutes()))
                .departureTime(item.departureTime())
                .status(item.status())
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .routePoints(item.routePoints() == null ? null : item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }

    public FetchTripResponse.FetchTripResponseData toFetchTripDetailResponseData(FetchTripResult item) {
        return FetchTripResponse.FetchTripResponseData.builder()
                .id(item.id())
                .creator(item.creator())
                .pickupBranch(item.pickupBranch())
                .tripCode(item.tripCode())
                .originCode(item.originCode())
                .originName(item.originName())
                .destinationCode(item.destinationCode())
                .destinationName(item.destinationName())
                .originProvinceId(item.originProvinceId())
                .destinationProvinceId(item.destinationProvinceId())
                .originDepartmentId(item.originDepartmentId())
                .destinationDepartmentId(item.destinationDepartmentId())
                .rawDepartureTime(item.rawDepartureTime())
                .rawDepartureDate(item.rawDepartureDate())
                .rawArrivalTime(calculateArrivalTime(item.rawDepartureTime(), item.durationMinutes()))
                .departureTime(item.departureTime())
                .status(item.status())
                .vehicleId(item.vehicleId())
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .assignedAt(item.assignedAt())
                .routePoints(item.routePoints() == null ? null : item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }


    public SearchTripResponse.SearchTripResponseData toSearchTripResponseData(SearchTripItemResult item) {
        return SearchTripResponse.SearchTripResponseData.builder()
                .id(item.id())
                .merchantId(item.merchantId())
                .merchantName(item.merchantName())
                .pickupBranch(item.pickupBranch())
                .vehicleId(item.vehicleId())
                .driverId(item.driverId())
                .originCode(item.originCode())
                .originName(item.originName())
                .destinationCode(item.destinationCode())
                .destinationName(item.destinationName())
                .originProvinceId(item.originProvinceId())
                .destinationProvinceId(item.destinationProvinceId())
                .originDepartmentId(item.originDepartmentId())
                .destinationDepartmentId(item.destinationDepartmentId())
                .ticketPrice(item.ticketPrice())
                .availableSeats(item.availableSeats())
                .departureTime(item.departureTime())
                .rawDepartureDate(item.rawDepartureDate())
                .rawDepartureTime(item.rawDepartureTime())
                .rawArrivalTime(calculateArrivalTime(item.rawDepartureTime(), item.durationMinutes()))
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .tripCode(item.tripCode())
                .routePoints(item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }

    public SearchTripResponse.SearchRoutePoints toSearchRoutePoint(RoutePointResult point) {
        return SearchTripResponse.SearchRoutePoints.builder()
                .id(point.id())
                .operationOrder(point.operationOrder())
                .routeId(point.routeId())
                .plannedArrivalTime(point.plannedArrivalTime())
                .plannedDepartureTime(point.plannedDepartureTime())
                .note(point.note())
                .departmentId(point.departmentId())
                .stopName(point.stopName())
                .stopAddress(point.stopAddress())
                .stopCity(point.stopCity())
                .stopLatitude(point.stopLatitude())
                .stopLongitude(point.stopLongitude())
                .build();
    }

    private String calculateArrivalTime(String rawDepartureTime, Long durationMinutes) {
        if (rawDepartureTime == null || durationMinutes == null) return null;
        try {
            String[] parts = rawDepartureTime.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            int totalMinutes = hours * 60 + minutes + durationMinutes.intValue();
            int arrivalHours = (totalMinutes / 60) % 24;
            int arrivalMinutes = totalMinutes % 60;

            return String.format("%02d:%02d", arrivalHours, arrivalMinutes);
        } catch (Exception e) {
            return null;
        }
    }
}
