package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.infrastructure.kafka.event.TripSellableEvent;

public interface RouteSaleEventPort {
    void publishRouteReadyForSale(
            String requestId,
            String requestDateTime,
            String channel,
            String aggregateId,
            TripSellableEvent payload
    );
}
