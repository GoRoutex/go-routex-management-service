package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record SearchRouteQuery(
        String origin,
        String destination,
        String departureDate,
        String seat,
        String fromTime,
        String toTime,
        String pageSize,
        String pageNumber,
        String requestId,
        String requestDateTime,
        String channel
) {
}
