package vn.com.routex.hub.management.service.interfaces.mapper;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.command.route.FetchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.RoutePointResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteItemResult;
import vn.com.routex.hub.management.service.interfaces.models.route.FetchRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteResponse;

@Component
public class RouteResponseMapper {

    public FetchRouteResponse.FetchRouteResponseData toFetchRouteResponseData(FetchRouteResult item) {
        return FetchRouteResponse.FetchRouteResponseData.builder()
                .id(item.id())
                .creator(item.creator())
                .pickupBranch(item.pickupBranch())
                .routeCode(item.routeCode())
                .origin(item.origin())
                .destination(item.destination())
                .plannedStartTime(item.plannedStartTime())
                .plannedEndTime(item.plannedEndTime())
                .actualStartTime(item.actualStartTime())
                .actualEndTime(item.actualEndTime())
                .status(item.status())
                .availableSeats(item.availableSeats())
                .vehicleId(item.vehicleId())
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .assignedAt(item.assignedAt())
                .routePoints(item.routePoints() == null ? null : item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }

    public FetchRouteResponse.FetchRouteResponseData toPublicFetchRouteResponseData(FetchRouteResult item) {
        return FetchRouteResponse.FetchRouteResponseData.builder()
                .id(item.id())
                .routeCode(item.routeCode())
                .origin(item.origin())
                .destination(item.destination())
                .plannedStartTime(item.plannedStartTime())
                .plannedEndTime(item.plannedEndTime())
                .status(item.status())
                .availableSeats(item.availableSeats())
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .routePoints(item.routePoints() == null ? null : item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }

    public FetchRouteResponse.FetchRouteResponseData toFetchRouteDetailResponseData(FetchRouteResult item) {
        return FetchRouteResponse.FetchRouteResponseData.builder()
                .id(item.id())
                .creator(item.creator())
                .pickupBranch(item.pickupBranch())
                .routeCode(item.routeCode())
                .origin(item.origin())
                .destination(item.destination())
                .plannedStartTime(item.plannedStartTime())
                .plannedEndTime(item.plannedEndTime())
                .actualStartTime(item.actualStartTime())
                .actualEndTime(item.actualEndTime())
                .status(item.status())
                .availableSeats(item.availableSeats())
                .vehicleId(item.vehicleId())
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .assignedAt(item.assignedAt())
                .routePoints(item.routePoints() == null ? null : item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }


    public SearchRouteResponse.SearchRouteResponseData toSearchRouteResponseData(SearchRouteItemResult item) {
        return SearchRouteResponse.SearchRouteResponseData.builder()
                .id(item.id())
                .merchantId(item.merchantId())
                .merchantName(item.merchantName())
                .pickupBranch(item.pickupBranch())
                .vehicleId(item.vehicleId())
                .driverId(item.driverId())
                .origin(item.origin())
                .destination(item.destination())
                .ticketPrice(item.ticketPrice())
                .availableSeats(item.availableSeats())
                .plannedStartTime(item.plannedStartTime())
                .plannedEndTime(item.plannedEndTime())
                .vehiclePlate(item.vehiclePlate())
                .hasFloor(item.hasFloor())
                .routeCode(item.routeCode())
                .routePoints(item.routePoints().stream()
                        .map(this::toSearchRoutePoint)
                        .toList())
                .build();
    }

    public SearchRouteResponse.SearchRoutePoints toSearchRoutePoint(RoutePointResult point) {
        return SearchRouteResponse.SearchRoutePoints.builder()
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
