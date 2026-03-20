package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.route.readmodel.RouteSearchView;

import java.time.OffsetDateTime;
import java.util.List;

public interface RouteQueryPort {
    List<RouteSearchView> searchAssignedRoutes(
            String origin,
            String destination,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            int pageNumber,
            int pageSize
    );
}
