package vn.com.routex.hub.management.service.infrastructure.kafka.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentEventPort;
import vn.com.routex.hub.management.service.infrastructure.kafka.config.KafkaEventPublisher;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteAssignedEvent;

@RequiredArgsConstructor
@Component
public class RouteAssignedEventAdapter implements RouteAssignmentEventPort {

    private final KafkaEventPublisher kafkaEventPublisher;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Value("${spring.kafka.topics.routes}")
    private String routeTopic;

    @Value("${spring.kafka.events.route-assigned}")
    private String routeAssignedEvent;

    @Override
    public void publishAssignedRoute(String requestId, String requestDateTime, String channel, String aggregateId, RouteAssignedEvent payload) {
        kafkaEventPublisher.publish(
                requestId,
                 requestDateTime,
                channel,
                routeTopic,
                routeAssignedEvent,
                aggregateId,
                payload
        );

        sLog.info("[KAFKA-EVENTS] Route Assigned Event published successfully");
    }

}
