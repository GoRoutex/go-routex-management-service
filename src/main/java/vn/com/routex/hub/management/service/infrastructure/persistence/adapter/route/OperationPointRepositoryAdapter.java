package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.port.OperationPointRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.entity.OperationPointEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.repository.OperationPointEntityRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OperationPointRepositoryAdapter implements OperationPointRepositoryPort {

    private final OperationPointEntityRepository operationPointEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public void saveAll(List<RouteStopPlan> stopPlans) {
        List<OperationPointEntity> entities = stopPlans.stream()
                .map(routePersistenceMapper::toEntity)
                .toList();
        operationPointEntityRepository.saveAll(entities);
    }

    @Override
    public void save(RouteStopPlan routeStopPlan) {
        operationPointEntityRepository.save(routePersistenceMapper.toEntity(routeStopPlan));
    }

    @Override
    public List<RouteStopPlan> findByRouteId(String routeId) {
        List<RouteStopPlan> stopPlans = operationPointEntityRepository.findAllByRouteId(routeId).stream()
                .map(routePersistenceMapper::toStopPlan)
                .toList();
        stopPlans.sort(Comparator.comparingInt(RouteStopPlan::getStopOrder));
        return stopPlans;
    }

    @Override
    public Map<String, List<RouteStopPlan>> findByRouteIds(List<String> routeIds) {
        List<RouteStopPlan> stopPlans = operationPointEntityRepository.findByRouteIdIn(routeIds).stream()
                .map(routePersistenceMapper::toStopPlan)
                .toList();

        return stopPlans.stream()
                .sorted(Comparator.comparingInt(RouteStopPlan::getStopOrder))
                .collect(Collectors.groupingBy(RouteStopPlan::getRouteId));
    }

    @Override
    public Optional<RouteStopPlan> findByRouteIdAndStopOrder(String routeId, String stopOrder) {
        return operationPointEntityRepository.findByRouteIdAndStopOrder(routeId, stopOrder).map(routePersistenceMapper::toStopPlan);
    }
}
