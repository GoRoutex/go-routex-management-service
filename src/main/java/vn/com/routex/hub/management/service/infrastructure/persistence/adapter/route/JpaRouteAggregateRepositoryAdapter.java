package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.repository.RouteJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaRouteAggregateRepositoryAdapter implements RouteAggregateRepositoryPort {

    private final RouteJpaRepository routeJpaRepository;

    @Override
    public Optional<RouteAggregate> findById(String routeId) {
        return routeJpaRepository.findById(routeId)
                .map(RoutePersistenceMapper::toAggregate);
    }

    @Override
    public void save(RouteAggregate aggregate) {
        routeJpaRepository.save(RoutePersistenceMapper.toEntity(aggregate));
    }

    @Override
    public String generateRouteCode(String originCode, String destinationCode) {
        return routeJpaRepository.generateRouteCode(originCode, destinationCode);
    }
}
