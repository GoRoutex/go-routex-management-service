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
                .departureTime(item.departureTime())
                .rawDepartureDate(item.rawDepartureDate())
                .rawDepartureTime(item.rawDepartureTime())
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
                .rawDepartureTime(item.rawDepartureTime())
                .rawDepartureDate(item.rawDepartureDate())
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
                .rawDepartureTime(item.rawDepartureTime())
                .rawDepartureDate(item.rawDepartureDate())
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


    public SearchTripResponse.SearchRouteResponseData toSearchRouteResponseData(SearchTripItemResult item) {
        return SearchTripResponse.SearchRouteResponseData.builder()
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
                .ticketPrice(item.ticketPrice())
                .availableSeats(item.availableSeats())
                .departureTime(item.departureTime())
                .rawDepartureDate(item.rawDepartureDate())
                .rawDepartureTime(item.rawDepartureTime())
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
                .operationPointId(point.operationPointId())
                .stopName(point.stopName())
                .stopAddress(point.stopAddress())
                .stopCity(point.stopCity())
                .stopLatitude(point.stopLatitude())
                .stopLongitude(point.stopLongitude())
                .build();
    }
}
