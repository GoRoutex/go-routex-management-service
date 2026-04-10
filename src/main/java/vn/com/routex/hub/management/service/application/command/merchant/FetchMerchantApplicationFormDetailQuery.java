package vn.com.routex.hub.management.service.application.command.merchant;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchMerchantApplicationFormDetailQuery(
        String applicationFormId,
        RequestContext context
) {
}
