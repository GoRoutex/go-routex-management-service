package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

@Builder
public record FetchRoutesQuery(
        String pageSize,
        String pageNumber,
        String requestId,
        String requestDateTime,
        String channel
) {
}
