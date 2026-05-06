package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity.RouteEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteAggregateRepositoryAdapter implements RouteAggregateRepositoryPort {

    private final RouteEntityRepository routeEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public Optional<RouteAggregate> findById(String routeId) {
        return routeEntityRepository.findById(routeId)
                .map(routePersistenceMapper::toAggregate);
    }

    @Override
    public Optional<RouteAggregate> findById(String routeId, String merchantId) {
        return routeEntityRepository.findByIdAndMerchantId(routeId, merchantId)
                .map(routePersistenceMapper::toAggregate);
    }

    @Override
    public List<RouteAggregate> findByMerchantId(String merchantId) {
        return routeEntityRepository.findByMerchantId(merchantId).stream()
                .map(routePersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    public void save(RouteAggregate aggregate) {
        routeEntityRepository.save(routePersistenceMapper.toEntity(aggregate));
    }

    @Override
    public Map<String, RouteAggregate> findAllByIdIn(List<String> routeIds) {
        List<RouteEntity> routes = routeEntityRepository.findAllByIdIn(routeIds);
        return routes.stream()
                .map(routePersistenceMapper::toAggregate)
                .collect(Collectors.toMap(
                        RouteAggregate::getId,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(RouteAggregate::getCreatedAt))
                ));
    }
}
