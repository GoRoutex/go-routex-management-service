package vn.com.routex.hub.management.service.application.command.route;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchRouteResult(
        List<SearchRouteItemResult> data
) {
}
