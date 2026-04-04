package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.outbox.OutBoxEventStatus;
import vn.com.routex.hub.management.service.domain.outbox.model.OutboxEvent;
import vn.com.routex.hub.management.service.domain.outbox.port.OutBoxEventRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.outbox.repository.OutBoxEventRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutBoxEventRepositoryAdapter implements OutBoxEventRepositoryPort {

    private final OutboxEventPersistenceMapper outboxEventPersistenceMapper;
    private final OutBoxEventRepository outBoxEventRepository;

    @Override
    public void save(OutboxEvent outboxEvent) {
        outBoxEventRepository.save(outboxEventPersistenceMapper.toEntity(outboxEvent));
    }

    @Override
    public void markAsProcessed(List<String> processedIds, OffsetDateTime now) {
        if(processedIds.isEmpty()) {
            return;
        }
        outBoxEventRepository.markAsProcessed(
                processedIds,
                OutBoxEventStatus.PROCESSED,
                now,
                now
        );
    }

    @Override
    public void markAsFailed(List<String> failedIds, OffsetDateTime now) {
        if(failedIds.isEmpty()) {
            return;
        }
        outBoxEventRepository.markAsFailed(
                failedIds,
                OutBoxEventStatus.FAILED,
                now
        );
    }

    @Override
    public List<OutboxEvent> lockPendingBatch(int i) {
        return outBoxEventRepository.lockPendingBatch(i)
                .stream()
                .map(outboxEventPersistenceMapper::toDomain)
                .toList();
    }
}
