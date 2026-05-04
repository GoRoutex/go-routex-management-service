package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.entity.TripSeatEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.projection.TripSeatAvailabilityProjection;

import java.util.List;
import java.util.Optional;


@Repository
public interface TripSeatEntityRepository extends JpaRepository<TripSeatEntity, String> {

    boolean existsByTripId(String tripId);
    List<TripSeatEntity> findAllByTripIdOrderBySeatNoAsc(String tripId);

    @Query(value = """
            SELECT rs.trip_id AS tripId, COUNT(*) AS availableSeat
            FROM trip_seat rs
            WHERE rs.trip_id IN :tripIds
              AND rs.STATUS = :status
            GROUP BY rs.trip_id
            """, nativeQuery = true)
    List<TripSeatAvailabilityProjection> countAvailableSeatsByTripIdAndStatus(@Param("tripIds") List<String> tripIds,
                                                                              @Param("status") String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
                   select rs
                   from TripSeatEntity rs
                   where rs.tripId = :tripId
                   and rs.seatNo in :seatNos
                   """)
    List<TripSeatEntity> findAllByTripIdAndSeatNoInForUpdate(@Param("tripId") String tripId,
                                                              @Param("seatNos") List<String> seatNos);

    Optional<TripSeatEntity> findByTripIdAndSeatNo(String tripId, String seatNo);
}
