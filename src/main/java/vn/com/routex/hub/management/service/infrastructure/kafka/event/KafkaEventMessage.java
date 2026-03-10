package vn.com.routex.hub.management.service.infrastructure.kafka.event;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record KafkaEventMessage<T> (
        String eventId,
        String eventName,
        String aggregateId,
        String source,
        Integer version,
        OffsetDateTime occurredAt,
        T data
) {
}
