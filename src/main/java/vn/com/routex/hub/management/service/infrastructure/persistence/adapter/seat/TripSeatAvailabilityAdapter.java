package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.port.TripSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.seat.SeatStatus;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.projection.TripSeatAvailabilityProjection;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.repository.TripSeatEntityRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TripSeatAvailabilityAdapter implements TripSeatAvailabilityPort {

    private final TripSeatEntityRepository tripSeatEntityRepository;

    @Override
    public Map<String, Long> countAvailableSeats(List<String> tripIds) {
        return tripSeatEntityRepository.countAvailableSeatsByTripIdAndStatus(tripIds, SeatStatus.AVAILABLE.name()).stream()
                .collect(Collectors.toMap(TripSeatAvailabilityProjection::getTripId, TripSeatAvailabilityProjection::getAvailableSeat));
    }
}
