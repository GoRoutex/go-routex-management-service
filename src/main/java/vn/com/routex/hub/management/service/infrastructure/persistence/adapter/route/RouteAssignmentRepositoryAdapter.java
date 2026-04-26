package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
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

    private final RouteAssignmentEntityRepository routeAssignmentEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public boolean existsActiveByRouteId(String routeId) {
        return routeAssignmentEntityRepository.existsByRouteId(routeId);
    }

    @Override
    public boolean existsActiveByRouteId(String routeId, String merchantId) {
        return routeAssignmentEntityRepository.existsByRouteIdAndMerchantId(routeId, merchantId);
    }

    @Override
    public Optional<RouteAssignmentRecord> findByRouteIdAndStatus(String routeId, RouteAssignmentStatus status) {
        Optional<RouteAssignmentEntity> entityOptional = routeAssignmentEntityRepository.findByRouteIdAndStatus(routeId, RouteAssignmentStatus.PENDING_ASSIGNMENT);

        if(entityOptional.isPresent()) {
            RouteAssignmentEntity routeAssignmentEntity = entityOptional.get();
            sLog.info("found entity");

            return Optional.ofNullable(routePersistenceMapper.toAssignmentRecord(routeAssignmentEntity));
        } else {
            sLog.info("not found");
        }

        return null;
    }

    @Override
    public Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId) {
        return routeAssignmentEntityRepository
                .findFirstByRouteIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(routeId, RouteAssignmentStatus.ASSIGNED)
                .map(routePersistenceMapper::toAssignmentRecord);
    }

    @Override
    public Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId, String merchantId) {
        return routeAssignmentEntityRepository
                .findFirstByRouteIdAndMerchantIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(
                        routeId,
                        merchantId,
                        RouteAssignmentStatus.ASSIGNED
                )
                .map(routePersistenceMapper::toAssignmentRecord);
    }

    @Override
    public Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds) {
        List<RouteAssignmentEntity> assignments = routeAssignmentEntityRepository.findActiveByRouteIdsNative(routeIds, RouteAssignmentStatus.ASSIGNED.name());
        return toAssignmentMap(assignments);
    }

    @Override
    public Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds, String merchantId) {
        List<RouteAssignmentEntity> assignments = routeAssignmentEntityRepository.findActiveByRouteIdsAndMerchantIdNative(
                routeIds,
                merchantId,
                RouteAssignmentStatus.ASSIGNED.name()
        );
        return toAssignmentMap(assignments);
    }

    @Override
    public List<RouteAssignmentRecord> findByMerchantId(String merchantId) {
        return routeAssignmentEntityRepository.findByMerchantId(merchantId).stream()
                .map(routePersistenceMapper::toAssignmentRecord)
                .toList();
    }

    private Map<String, RouteAssignmentRecord> toAssignmentMap(List<RouteAssignmentEntity> assignments) {
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
        routeAssignmentEntityRepository.save(routePersistenceMapper.toEntity(assignment));
    }
}
