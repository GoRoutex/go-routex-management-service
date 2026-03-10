package vn.com.routex.hub.management.service.infrastructure.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.JsonUtils;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    public void publish(
            BaseRequest baseRequest,
            String topic,
            String event,
            String aggregateId,
            Object payload
    ) {
        try {
            KafkaEventMessage<Object> message =
                    KafkaEventMessage.builder()
                            .requestId(baseRequest.getRequestId())
                            .requestDateTime(baseRequest.getRequestDateTime())
                            .channel(baseRequest.getChannel())
                            .eventId(UUID.randomUUID().toString())
                            .eventName(event)
                            .aggregateId(aggregateId)
                            .source("management-service")
                            .version(1)
                            .occurredAt(OffsetDateTime.now())
                            .data(payload)
                            .build();

            String json = JsonUtils.parseToJsonStr(message);

            kafkaTemplate.send(topic, aggregateId, json);
        } catch(Exception ex) {
            throw new IllegalArgumentException("Kafka publish failed: ", ex);
        }
    }
}
