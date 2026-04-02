package vn.com.routex.hub.management.service.application.command.provinces;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;


@Builder
public record FetchProvincesQuery(
        String pageSize,
        String pageNumber,
        RequestContext context
) {
}
