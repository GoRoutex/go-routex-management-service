package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchRoutesQuery(
        RequestContext context,
        String pageSize,
        String pageNumber,
        String merchantId,
        String merchantName
) {
}
