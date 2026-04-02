package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchVehiclesQuery(
        String pageSize,
        String pageNumber,
        RequestContext context
) {
}

