package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentJpaEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteAssignmentJpaRepository extends JpaRepository<RouteAssignmentJpaEntity, String> {
    boolean existsByRouteId(String routeId);

    Optional<RouteAssignmentJpaEntity> findFirstByRouteIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(String routeId, RouteAssignmentStatus status);

    @Query(value = """
            SELECT ra.*
            FROM route_assignment ra
            WHERE ra.route_id IN (:routeIds)
              AND ra.status = :status
              AND ra.unassigned_at IS NULL
        """, nativeQuery = true)
    List<RouteAssignmentJpaEntity> findActiveByRouteIdsNative(
            @Param("routeIds") List<String> routeIds,
            @Param("status") String status
    );
}
