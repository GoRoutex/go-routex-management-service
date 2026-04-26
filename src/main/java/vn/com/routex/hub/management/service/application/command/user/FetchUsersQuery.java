package vn.com.routex.hub.management.service.application.command.user;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record FetchUsersQuery(
        String pageSize,
        String pageNumber,
        RequestContext context
) {
}
