package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.PageContext;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record SearchRouteQuery(
        RequestContext context,
        String merchantId,
        String origin,
        String destination,
        String departureDate,
        String seat,
        String fromTime,
        String toTime,
        PageContext pageContext
) {
}
