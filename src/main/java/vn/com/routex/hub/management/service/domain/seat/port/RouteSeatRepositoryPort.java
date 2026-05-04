package vn.com.routex.hub.management.service.domain.seat.port;


import vn.com.routex.hub.management.service.domain.seat.model.TripSeat;

import java.util.List;
import java.util.Optional;

public interface RouteSeatRepositoryPort {
    boolean existsByTripId(String tripId);

    List<TripSeat> findAllByTripIdOrderBySeatNoAsc(String tripId);

    List<TripSeat> findAllByTripIdAndSeatNoInForUpdate(String tripId, List<String> seatNos);

    Optional<TripSeat> findByTripIdAndSeatNo(String tripId, String seatNo);

    List<TripSeat> saveAll(List<TripSeat> tripSeats);

    TripSeat save(TripSeat tripSeat);
}
