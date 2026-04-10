package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

import java.util.List;

@Builder
public record CreateRouteCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String pickupBranch,
        String origin,
        String destination,
        String plannedStartTime,
        String plannedEndTime,
        List<RoutePointCommand> routePoints
) {
}
