package vn.com.routex.hub.user.service.application.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import vn.com.routex.hub.user.service.domain.route.Route;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
public class RouteSpecification {

    public static Specification<Route> originContainsIgnoreCase(String origin) {
        String v = normalize(origin);
        return (root, query, cb) -> cb.like(cb.lower(root.get("origin")), "%" + v + "%");
    }

    public static Specification<Route> destinationContainsIgnoreCase(String destination) {
        String v = normalize(destination);
        return (root, query, cb) -> cb.like(cb.lower(root.get("destination")), "%" + v + "%");
    }

    public static Specification<Route> plannedStartBetween(OffsetDateTime startInitialize, OffsetDateTime endInitialize) {
        return (root, query, cb) -> cb.and(
            cb.greaterThanOrEqualTo(root.get("plannedStartTime"), startInitialize),
            cb.lessThan(root.get("plannedEndTime"), endInitialize));
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
