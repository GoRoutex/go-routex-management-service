package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.repository.RouteAssignmentEntityRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteAssignmentRepositoryAdapter implements RouteAssignmentRepositoryPort {

    private final RouteAssignmentEntityRepository routeAssignmentJpaRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public boolean existsActiveByRouteId(String routeId) {
        return routeAssignmentJpaRepository.existsByRouteId(routeId);
    }

    @Override
    public Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId) {
        return routeAssignmentJpaRepository
                .findFirstByRouteIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(routeId, RouteAssignmentStatus.ASSIGNED)
                .map(routePersistenceMapper::toAssignmentRecord);
    }

    @Override
    public Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds) {
        List<RouteAssignmentEntity> assignments = routeAssignmentJpaRepository.findActiveByRouteIdsNative(routeIds, RouteAssignmentStatus.ASSIGNED.name());
        return assignments.stream()
                .map(routePersistenceMapper::toAssignmentRecord)
                .collect(Collectors.toMap(
                        RouteAssignmentRecord::getRouteId,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(RouteAssignmentRecord::getAssignedAt))
                ));
    }

    @Override
    public void save(RouteAssignmentRecord assignment) {
        routeAssignmentJpaRepository.save(routePersistenceMapper.toEntity(assignment));
    }
}
