package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteAssignedEvent;

public interface RouteAssignmentEventPort {
    void publishAssignedRoute(
            String requestId,
            String requestDateTime,
            String channel,
            String routeId,
            RouteAssignedEvent event
    );
}
