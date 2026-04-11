package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.PageContext;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchRoutesQuery(
        RequestContext context,
        PageContext pageContext,
        String merchantId,
        String merchantName
) {
}
