package vn.com.routex.hub.management.service.infrastructure.kafka.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.domain.outbox.model.OutboxEvent;
import vn.com.routex.hub.management.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.JsonUtils;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    public CompletableFuture<Void> publishAsync(OutboxEvent outboxEvent) {

        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(outboxEvent.getPayload());
        } catch (Exception e) {
            sLog.error("[OUTBOX-SERIALIZE-ERROR] eventId={}", outboxEvent.getId(), e);
            return CompletableFuture.failedFuture(e);
        }
        CompletableFuture<SendResult<String, String>> kafkaFuture = kafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getAggregateId(), jsonPayload);
        return kafkaFuture.thenAccept(result -> {
            RecordMetadata recordMetadata = result.getRecordMetadata();
            sLog.debug("[OUTBOX-PUBLISH-SUCCESS] eventId={}, topic={}, partition={}, offset={}",
                    outboxEvent.getId(),
                    outboxEvent.getTopic(),
                    recordMetadata.partition(),
                    recordMetadata.offset());
        }).exceptionally(ex -> {
            sLog.error(
                    "[OUTBOX-PUBLISH-FAILED] eventId={}, topic={}",
                    outboxEvent.getId(),
                    outboxEvent.getTopic(),
                    ex
            );
            throw new RuntimeException(ex);
        });
    }

    public void publish(OutboxEvent outboxEvent) {
        try {

            BaseRequest baseRequest = objectMapper.convertValue(outboxEvent.getHeader().get("context"), BaseRequest.class);

            KafkaEventMessage<Object> message =
                    KafkaEventMessage.builder()
                            .requestId(baseRequest.getRequestId())
                            .requestDateTime(baseRequest.getRequestDateTime())
                            .channel(baseRequest.getChannel())
                            .eventId(UUID.randomUUID().toString())
                            .eventType(outboxEvent.getEventType())
                            .aggregateId(outboxEvent.getAggregateId())
                            .source("management-service")
                            .version(1)
                            .occurredAt(OffsetDateTime.now())
                            .data(outboxEvent.getPayload().get("data"))
                            .build();

            String json = JsonUtils.parseToJsonStr(message);

            kafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getAggregateId(), json);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Kafka publish failed: ", ex);
        }
    }
}
