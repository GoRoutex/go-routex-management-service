package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RouteAggregateRepositoryAdapter implements RouteAggregateRepositoryPort {

    private final RouteEntityRepository RouteEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public Optional<RouteAggregate> findById(String routeId) {
        return RouteEntityRepository.findById(routeId)
                .map(routePersistenceMapper::toAggregate);
    }

    @Override
    public Optional<RouteAggregate> findById(String routeId, String merchantId) {
        return RouteEntityRepository.findByIdAndMerchantId(routeId, merchantId)
                .map(routePersistenceMapper::toAggregate);
    }

    @Override
    public List<RouteAggregate> findByMerchantId(String merchantId) {
        return RouteEntityRepository.findByMerchantId(merchantId).stream()
                .map(routePersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    public void save(RouteAggregate aggregate) {
        RouteEntityRepository.save(routePersistenceMapper.toEntity(aggregate));
    }

    @Override
    public String generateRouteCode(String originCode, String destinationCode) {
        return RouteEntityRepository.generateRouteCode(originCode, destinationCode);
    }
}
