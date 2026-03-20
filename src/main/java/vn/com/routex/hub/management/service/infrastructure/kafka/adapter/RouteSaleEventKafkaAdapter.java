package vn.com.routex.hub.management.service.infrastructure.kafka.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.port.RouteSaleEventPort;
import vn.com.routex.hub.management.service.infrastructure.kafka.config.KafkaEventPublisher;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteSellableEvent;

@Component
@RequiredArgsConstructor
public class RouteSaleEventKafkaAdapter implements RouteSaleEventPort {

    private final KafkaEventPublisher kafkaEventPublisher;

    @Value("${spring.kafka.topics.routes}")
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
    }
}
