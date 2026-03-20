package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;

import java.util.Optional;

public interface RouteAggregateRepositoryPort {
    Optional<RouteAggregate> findById(String routeId);

    void save(RouteAggregate aggregate);

    String generateRouteCode(String originCode, String destinationCode);
}
