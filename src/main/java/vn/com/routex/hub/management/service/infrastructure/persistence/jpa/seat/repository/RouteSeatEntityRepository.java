package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.entity.RouteSeatEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.projection.RouteSeatAvailabilityProjection;

import java.util.List;
import java.util.Optional;


@Repository
public interface RouteSeatEntityRepository extends JpaRepository<RouteSeatEntity, String> {

    boolean existsByRouteId(String routeId);
    List<RouteSeatEntity> findAllByRouteIdOrderBySeatNoAsc(String routeId);

    @Query(value = """
            SELECT rs.ROUTE_ID AS routeId, COUNT(*) AS availableSeat
            FROM ROUTE_SEAT rs
            WHERE rs.ROUTE_ID IN :routeIds
              AND rs.STATUS = :status
            GROUP BY rs.ROUTE_ID
            """, nativeQuery = true)
    List<RouteSeatAvailabilityProjection> countAvailableSeatsByRouteIdAndStatus(@Param("routeIds") List<String> routeIds,
                                                                                @Param("status") String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
                   select rs
                   from RouteSeatEntity rs
                   where rs.routeId = :routeId
                   and rs.seatNo in :seatNos
                   """)
    List<RouteSeatEntity> findAllByRouteIdAndSeatNoInForUpdate(@Param("routeId") String routeId,
                                                                  @Param("seatNos") List<String> seatNos);

    Optional<RouteSeatEntity> findByRouteIdAndSeatNo(String routeId, String seatNo);
}
