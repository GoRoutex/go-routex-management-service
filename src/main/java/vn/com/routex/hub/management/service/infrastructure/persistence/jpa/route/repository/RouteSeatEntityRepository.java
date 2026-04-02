package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity.RouteSeatEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.projection.RouteSeatAvailabilityProjection;

import java.util.List;

@Repository
public interface RouteSeatEntityRepository extends JpaRepository<RouteSeatEntity, Integer> {

    boolean existsByRouteId(String routeId);

    @Query(value = """
            SELECT rs.ROUTE_ID AS routeId, COUNT(*) AS availableSeat
            FROM ROUTE_SEAT rs
            WHERE rs.ROUTE_ID IN :routeIds
              AND rs.STATUS = :status
            GROUP BY rs.ROUTE_ID
            """, nativeQuery = true)
    List<RouteSeatAvailabilityProjection> countAvailableSeatsByRouteIdAndStatus(@Param("routeIds") List<String> routeIds,
                                                                                @Param("status") String status);

    List<RouteSeatEntity> findAllByRouteIdOrderBySeatNoAsc(String routeId);

    List<RouteSeatEntity> findAllByRouteIdAndSeatNoIn(String routeId, List<String> seatNos);
}

