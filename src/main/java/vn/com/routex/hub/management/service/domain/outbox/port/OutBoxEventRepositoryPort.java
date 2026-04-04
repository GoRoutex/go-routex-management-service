package vn.com.routex.hub.management.service.domain.outbox.port;

import vn.com.routex.hub.management.service.domain.outbox.model.OutboxEvent;

import java.time.OffsetDateTime;
import java.util.List;

public interface OutBoxEventRepositoryPort {
    void save(OutboxEvent outboxEvent);

    void markAsProcessed(List<String> processedIds, OffsetDateTime now);
    void markAsFailed(List<String> failedIds, OffsetDateTime now);
    List<OutboxEvent> lockPendingBatch(int i);
}
