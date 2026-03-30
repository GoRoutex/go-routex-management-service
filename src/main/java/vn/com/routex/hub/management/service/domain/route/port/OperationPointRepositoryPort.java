package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;

import java.util.List;
import java.util.Map;

public interface OperationPointRepositoryPort {
    void saveAll(List<RouteStopPlan> stopPlans);

    List<RouteStopPlan> findByRouteId(String routeId);

    Map<String, List<RouteStopPlan>> findByRouteIds(List<String> routeIds);
}
