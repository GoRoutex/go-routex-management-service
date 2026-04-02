package vn.com.routex.hub.management.service.application.command.common;

import lombok.Builder;

@Builder
public record RequestContext(
        String requestId,
        String requestDateTime,
        String channel
) {
}

