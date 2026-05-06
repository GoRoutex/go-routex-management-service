package vn.com.routex.hub.management.service.application.command.department;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchDepartmentQuery(
        String pageSize,
        String pageNumber,
        String merchantId,
        RequestContext context
) {
}
