package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateRouteResult(
        String id,
        String creator,
        String pickupBranch,
        String routeCode,
        String origin,
        String destination,
        String plannedStartTime,
        String plannedEndTime,
        String status,
        List<OperationPointCommand> operationPoints
) {
}
