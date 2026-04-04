package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.outbox;


import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.outbox.model.OutboxEvent;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.outbox.entity.OutBoxEventEntity;

@Component
public class OutboxEventPersistenceMapper {

    public OutboxEvent toDomain(OutBoxEventEntity outboxEventEntity) {
        if(outboxEventEntity == null) {
            return null;
        }

        return OutboxEvent.builder()
                .id(outboxEventEntity.getId())
                .aggregateId(outboxEventEntity.getAggregateId())
                .eventType(outboxEventEntity.getEventType())
                .eventKey(outboxEventEntity.getEventKey())
                .payload(outboxEventEntity.getPayload())
                .status(outboxEventEntity.getStatus())
                .retryCount(outboxEventEntity.getRetryCount())
                .availableAt(outboxEventEntity.getAvailableAt())
                .processedAt(outboxEventEntity.getProcessedAt())
                .createdAt(outboxEventEntity.getCreatedAt())
                .createdBy(outboxEventEntity.getCreatedBy())
                .updatedAt(outboxEventEntity.getUpdatedAt())
                .updatedBy(outboxEventEntity.getUpdatedBy())
                .build();
    }

    public OutBoxEventEntity toEntity(OutboxEvent outboxEvent) {
        if(outboxEvent == null) {
            return null;
        }

        return OutBoxEventEntity.builder()
                .id(outboxEvent.getId())
                .aggregateId(outboxEvent.getAggregateId())
                .eventType(outboxEvent.getEventType())
                .eventKey(outboxEvent.getEventKey())
                .payload(outboxEvent.getPayload())
                .status(outboxEvent.getStatus())
                .retryCount(outboxEvent.getRetryCount())
                .availableAt(outboxEvent.getAvailableAt())
                .processedAt(outboxEvent.getProcessedAt())
                .createdAt(outboxEvent.getCreatedAt())
                .createdBy(outboxEvent.getCreatedBy())
                .updatedAt(outboxEvent.getUpdatedAt())
                .updatedBy(outboxEvent.getUpdatedBy())
                .build();
    }
}
