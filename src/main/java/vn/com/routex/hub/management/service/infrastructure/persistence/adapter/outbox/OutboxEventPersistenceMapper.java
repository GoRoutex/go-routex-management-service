package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.outbox;


import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.outbox.model.OutBoxEvent;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.outbox.entity.OutBoxEventEntity;

@Component
public class OutboxEventPersistenceMapper {

    public OutBoxEvent toDomain(OutBoxEventEntity outboxEventEntity) {
        if(outboxEventEntity == null) {
            return null;
        }

        return OutBoxEvent.builder()
                .id(outboxEventEntity.getId())
                .topic(outboxEventEntity.getTopic())
                .aggregateId(outboxEventEntity.getAggregateId())
                .eventType(outboxEventEntity.getEventType())
                .eventKey(outboxEventEntity.getEventKey())
                .header(outboxEventEntity.getHeader())
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

    public OutBoxEventEntity toEntity(OutBoxEvent outboxEvent) {
        if(outboxEvent == null) {
            return null;
        }

        return OutBoxEventEntity.builder()
                .id(outboxEvent.getId())
                .aggregateId(outboxEvent.getAggregateId())
                .eventType(outboxEvent.getEventType())
                .eventKey(outboxEvent.getEventKey())
                .topic(outboxEvent.getTopic())
                .payload(outboxEvent.getPayload())
                .header(outboxEvent.getHeader())
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
