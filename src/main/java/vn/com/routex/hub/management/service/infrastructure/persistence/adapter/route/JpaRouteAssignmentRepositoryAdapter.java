package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.repository.RouteAssignmentJpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaRouteAssignmentRepositoryAdapter implements RouteAssignmentRepositoryPort {

    private final RouteAssignmentJpaRepository routeAssignmentJpaRepository;

    @Override
    public boolean existsActiveByRouteId(String routeId) {
        return routeAssignmentJpaRepository.existsByRouteId(routeId);
    }

    @Override
    public Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId) {
        return routeAssignmentJpaRepository
                .findFirstByRouteIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(routeId, RouteAssignmentStatus.ASSIGNED)
                .map(RoutePersistenceMapper::toAssignmentRecord);
    }

    @Override
    public Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds) {
        List<RouteAssignmentJpaEntity> assignments = routeAssignmentJpaRepository.findActiveByRouteIdsNative(routeIds, RouteAssignmentStatus.ASSIGNED.name());
        return assignments.stream()
                .map(RoutePersistenceMapper::toAssignmentRecord)
                .collect(Collectors.groupingBy(RouteAssignmentRecord::getRouteId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .max(Comparator.comparing(RouteAssignmentRecord::getAssignedAt))
                                .orElse(null)
                ));
    }

    @Override
    public void save(RouteAssignmentRecord assignment) {
        routeAssignmentJpaRepository.save(RoutePersistenceMapper.toEntity(assignment));
    }
}
