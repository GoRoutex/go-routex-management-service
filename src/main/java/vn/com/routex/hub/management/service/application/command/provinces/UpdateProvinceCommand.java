package vn.com.routex.hub.management.service.application.command.provinces;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record UpdateProvinceCommand(
        RequestContext context,
        String id,

        String name,
        String code
) {
}

