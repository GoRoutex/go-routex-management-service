package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.port.RouteStopRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.stoppoint.entity.RouteStopJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.stoppoint.repository.RouteStopJpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaRouteStopRepositoryAdapter implements RouteStopRepositoryPort {

    private final RouteStopJpaRepository routeStopJpaRepository;

    @Override
    public void saveAll(List<RouteStopPlan> stopPlans) {
        List<RouteStopJpaEntity> entities = stopPlans.stream()
                .map(RoutePersistenceMapper::toEntity)
                .toList();
        routeStopJpaRepository.saveAll(entities);
    }

    @Override
    public List<RouteStopPlan> findByRouteId(String routeId) {
        List<RouteStopPlan> stopPlans = routeStopJpaRepository.findAllByRouteId(routeId).stream()
                .map(RoutePersistenceMapper::toStopPlan)
                .toList();
        stopPlans.sort(Comparator.comparingInt(RouteStopPlan::getStopOrder));
        return stopPlans;
    }

    @Override
    public Map<String, List<RouteStopPlan>> findByRouteIds(List<String> routeIds) {
        List<RouteStopPlan> stopPlans = routeStopJpaRepository.findByRouteIdIn(routeIds).stream()
                .map(RoutePersistenceMapper::toStopPlan)
                .toList();

        return stopPlans.stream()
                .sorted(Comparator.comparingInt(RouteStopPlan::getStopOrder))
                .collect(Collectors.groupingBy(RouteStopPlan::getRouteId));
    }
}
