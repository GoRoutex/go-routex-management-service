package vn.com.routex.hub.management.service.application.command.seat;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;


@Builder
public record SearchSeatCommand(
        RequestContext context,
        int pageNumber,
        int pageSize,
        String routeId
) {
}
