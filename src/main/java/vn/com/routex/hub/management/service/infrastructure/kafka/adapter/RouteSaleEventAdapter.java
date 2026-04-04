package vn.com.routex.hub.management.service.infrastructure.kafka.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.domain.route.port.RouteSaleEventPort;
import vn.com.routex.hub.management.service.infrastructure.kafka.config.KafkaEventPublisher;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteSellableEvent;

@Component
@RequiredArgsConstructor
public class RouteSaleEventAdapter implements RouteSaleEventPort {

    private final KafkaEventPublisher kafkaEventPublisher;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Value("${spring.kafka.topics.booking}")
    private String bookingTopic;

    @Value("${spring.kafka.events.route-ready-for-sale}")
    private String routeReadyForSale;

    @Override
    public void publishRouteReadyForSale(
            String requestId,
            String requestDateTime,
            String channel,
            String aggregateId,
            RouteSellableEvent payload
    ) {
        kafkaEventPublisher.publish(
                requestId,
                requestDateTime,
                channel,
                bookingTopic,
                routeReadyForSale,
                aggregateId,
                payload
        );

        sLog.info("[KAFKA-EVENTS] Route Ready For Sale Event published successfully");

    }
}
