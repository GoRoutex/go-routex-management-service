package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record RoutePointResult(
        String id,
        String operationOrder,
        String routeId,
        String plannedArrivalTime,
        String plannedDepartureTime,
        String note,
        String operationPointId,
        String stopName,
        String stopAddress,
        String stopCity,
        Double stopLatitude,
        Double stopLongitude
) {
}
