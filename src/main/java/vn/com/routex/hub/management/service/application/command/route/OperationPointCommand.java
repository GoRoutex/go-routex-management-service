package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record OperationPointCommand(
        String operationOrder,
        String plannedArrivalTime,
        String plannedDepartureTime,
        String note
) {
}
