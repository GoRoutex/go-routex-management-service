package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record OperationPointResult(
        String id,
        String operationOrder,
        String routeId,
        String plannedArrivalTime,
        String plannedDepartureTime,
        String note
) {
}
