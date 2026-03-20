package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RouteAssignmentRepositoryPort {
    boolean existsActiveByRouteId(String routeId);

    Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId);

    Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds);

    void save(RouteAssignmentRecord assignment);
}
