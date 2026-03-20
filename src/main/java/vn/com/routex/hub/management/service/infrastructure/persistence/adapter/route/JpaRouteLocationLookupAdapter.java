package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.LocationCodePair;
import vn.com.routex.hub.management.service.domain.route.port.RouteLocationLookupPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.location.projection.LocationCodeProjection;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.location.repository.LocationJpaRepository;

@Component
@RequiredArgsConstructor
public class JpaRouteLocationLookupAdapter implements RouteLocationLookupPort {

    private final LocationJpaRepository locationJpaRepository;

    @Override
    public LocationCodePair getCodes(String origin, String destination) {
        LocationCodeProjection view = locationJpaRepository.selectLocationCode(origin, destination);
        return new LocationCodePair(view.getOriginCode(), view.getDestinationCode());
    }
}
