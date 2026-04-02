package vn.com.routex.hub.management.service.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchOperationPointQuery(
        String pageSize,
        String pageNumber,
        RequestContext context
) {
}

