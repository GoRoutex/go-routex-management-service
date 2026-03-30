package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.port.OperationPointRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.entity.OperationPointJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.repository.OperationPointJpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaOperationPointRepositoryAdapter implements OperationPointRepositoryPort {

    private final OperationPointJpaRepository operationPointJpaRepository;

    @Override
    public void saveAll(List<RouteStopPlan> stopPlans) {
        List<OperationPointJpaEntity> entities = stopPlans.stream()
                .map(RoutePersistenceMapper::toEntity)
                .toList();
        operationPointJpaRepository.saveAll(entities);
    }

    @Override
    public List<RouteStopPlan> findByRouteId(String routeId) {
        List<RouteStopPlan> stopPlans = operationPointJpaRepository.findAllByRouteId(routeId).stream()
                .map(RoutePersistenceMapper::toStopPlan)
                .toList();
        stopPlans.sort(Comparator.comparingInt(RouteStopPlan::getStopOrder));
        return stopPlans;
    }

    @Override
    public Map<String, List<RouteStopPlan>> findByRouteIds(List<String> routeIds) {
        List<RouteStopPlan> stopPlans = operationPointJpaRepository.findByRouteIdIn(routeIds).stream()
                .map(RoutePersistenceMapper::toStopPlan)
                .toList();

        return stopPlans.stream()
                .sorted(Comparator.comparingInt(RouteStopPlan::getStopOrder))
                .collect(Collectors.groupingBy(RouteStopPlan::getRouteId));
    }
}
