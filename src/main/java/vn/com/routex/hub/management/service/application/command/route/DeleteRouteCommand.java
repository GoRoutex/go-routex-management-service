package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record DeleteRouteCommand(
        RequestContext context,
        String creator,
        String routeId,
        String merchantId
) {
}
