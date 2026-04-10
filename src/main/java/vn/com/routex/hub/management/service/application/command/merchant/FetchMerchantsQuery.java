package vn.com.routex.hub.management.service.application.command.merchant;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchMerchantsQuery(
        String pageSize,
        String pageNumber,
        RequestContext context
) {
}
