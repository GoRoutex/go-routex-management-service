package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record RoutePointCommand(
        String operationOrder,
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
