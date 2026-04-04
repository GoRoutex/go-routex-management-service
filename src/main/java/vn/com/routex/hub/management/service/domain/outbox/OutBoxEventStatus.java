package vn.com.routex.hub.management.service.domain.outbox;

public enum OutBoxEventStatus {
    PENDING,
    PROCESSED,
    COMPLETED,
    FAILED
}
