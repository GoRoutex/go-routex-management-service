package vn.com.routex.hub.management.service.application.command.merchant;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchPendingMerchantApplicationFormsQuery(
        String status,
        String pageSize,
        String pageNumber,
        RequestContext context
) {
}
