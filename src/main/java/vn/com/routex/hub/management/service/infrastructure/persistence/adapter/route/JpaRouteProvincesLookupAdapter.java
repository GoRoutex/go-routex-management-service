package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.ProvincesCodePair;
import vn.com.routex.hub.management.service.domain.route.port.RouteProvincesLookupPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.projection.ProvincesCodeProjection;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository.ProvincesEntityRepository;

@Component
@RequiredArgsConstructor
public class JpaRouteProvincesLookupAdapter implements RouteProvincesLookupPort {

    private final ProvincesEntityRepository provincesEntityRepository;

    @Override
    public ProvincesCodePair getCodes(String origin, String destination) {
        ProvincesCodeProjection view = provincesEntityRepository.selectProvincesCode(origin, destination);
        return new ProvincesCodePair(view.getOriginCode(), view.getDestinationCode());
    }
}
