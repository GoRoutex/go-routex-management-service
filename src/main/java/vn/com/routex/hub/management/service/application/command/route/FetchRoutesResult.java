package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.util.List;

@Builder
public record FetchRoutesResult(
        List<FetchRouteResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
