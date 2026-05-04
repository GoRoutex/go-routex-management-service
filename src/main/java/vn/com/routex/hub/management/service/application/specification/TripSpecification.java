package vn.com.routex.hub.management.service.application.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import vn.com.routex.hub.management.service.domain.trip.TripStatus;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.trip.entity.TripEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;


@RequiredArgsConstructor
public class TripSpecification {

    public static Specification<TripEntity> originNameContainsIgnoreCase(String originName) {
        String v = normalize(originName);
        return (root, query, cb) -> cb.like(cb.lower(root.get("originName")), "%" + v + "%");
    }

    public static Specification<TripEntity> destinationNameContainsIgnoreCase(String destinationName) {
        String v = normalize(destinationName);
        return (root, query, cb) -> cb.like(cb.lower(root.get("destinationName")), "%" + v + "%");
    }

    public static Specification<TripEntity> assignedStatus(TripStatus status) {
        return (root, query, cb) -> {
            if(status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<TripEntity> hasMerchantId(String merchantId) {
        return (root, query, cb) -> {
            if (merchantId == null || merchantId.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("merchantId"), merchantId);
        };
    }


    public static Specification<TripEntity> hasMerchantIds(List<String> merchantIds) {
        return (root, query, cb) -> {
            if (merchantIds == null) {
                return cb.conjunction();
            }
            if (merchantIds.isEmpty()) {
                return cb.disjunction();
            }
            return root.get("merchantId").in(merchantIds);
        };
    }
    public static OffsetDateTime dayStart(LocalDate date, ZoneId zoneId) {
        return date.atStartOfDay(zoneId).toOffsetDateTime();
    }

    public static OffsetDateTime dayEndExclusive(LocalDate date, ZoneId zoneId) {
        return date.plusDays(1).atStartOfDay(zoneId).toOffsetDateTime();
    }

    public static OffsetDateTime atTime(LocalDate date, LocalTime time, ZoneId zoneId) {
        return date.atTime(time).atZone(zoneId).toOffsetDateTime();
    }

    private static String normalize(String message) {
        return message == null ? "" : message.trim().toLowerCase();
    }
}
