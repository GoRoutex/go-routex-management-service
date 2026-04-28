package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.port.RouteSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.seat.SeatStatus;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.projection.RouteSeatAvailabilityProjection;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.repository.RouteSeatEntityRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteSeatAvailabilityAdapter implements RouteSeatAvailabilityPort {

    private final RouteSeatEntityRepository routeSeatEntityRepository;

    @Override
    public Map<String, Long> countAvailableSeats(List<String> routeIds) {
        return routeSeatEntityRepository.countAvailableSeatsByRouteIdAndStatus(routeIds, SeatStatus.AVAILABLE.name()).stream()
                .collect(Collectors.toMap(RouteSeatAvailabilityProjection::getRouteId, RouteSeatAvailabilityProjection::getAvailableSeat));
    }
}
