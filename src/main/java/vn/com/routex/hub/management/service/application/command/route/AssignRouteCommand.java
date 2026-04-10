package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record AssignRouteCommand(
        String merchantId,
        String creator,
        String routeId,
        String vehicleId,
        String driverId,
        RequestContext context
) {
}
