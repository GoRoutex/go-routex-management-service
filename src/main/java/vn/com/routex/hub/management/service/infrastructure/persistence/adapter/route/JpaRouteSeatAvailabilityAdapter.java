package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.port.RouteSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.seat.RouteSeatAvailabilityView;
import vn.com.routex.hub.management.service.domain.seat.RouteSeatRepository;
import vn.com.routex.hub.management.service.domain.seat.SeatStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaRouteSeatAvailabilityAdapter implements RouteSeatAvailabilityPort {

    private final RouteSeatRepository routeSeatRepository;

    @Override
    public Map<String, Long> countAvailableSeats(List<String> routeIds) {
        return routeSeatRepository.countByRouteIdAndStatus(routeIds, SeatStatus.AVAILABLE.name()).stream()
                .collect(Collectors.toMap(RouteSeatAvailabilityView::getRouteId, RouteSeatAvailabilityView::getAvailableSeat));
    }
}
