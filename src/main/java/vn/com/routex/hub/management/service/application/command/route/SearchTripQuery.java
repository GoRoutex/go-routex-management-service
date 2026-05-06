package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.PageContext;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record SearchTripQuery(
        RequestContext context,
        String originName,
        String destinationName,
        String originProvinceId,
        String destinationProvinceId,
        String departureDate,
        String seat,
        PageContext pageContext
) {
}
