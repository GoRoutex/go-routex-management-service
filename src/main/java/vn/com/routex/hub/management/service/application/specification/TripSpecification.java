package vn.com.routex.hub.management.service.application.specification;

import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import vn.com.routex.hub.management.service.domain.trip.TripStatus;
import vn.com.routex.hub.management.service.domain.trip.model.TripAggregate;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity.RouteEntity;

import java.util.List;


@RequiredArgsConstructor
public class TripSpecification {

    public static Specification<TripAggregate> originNameContainsIgnoreCase(String originName) {
        String v = normalize(originName);
        return (root, query, cb) -> {
            if (v.isBlank()) return cb.conjunction();
            Root<RouteEntity> routeRoot = query.from(RouteEntity.class);
            return cb.and(
                    cb.equal(root.get("routeId"), routeRoot.get("id")),
                    cb.like(cb.lower(routeRoot.get("originName")), "%" + v + "%")
            );
        };
    }

    public static Specification<TripAggregate> hasOriginProvinceId(String provinceId) {
        return (root, query, cb) -> {
            if (provinceId == null || provinceId.isBlank()) return cb.conjunction();
            Root<RouteEntity> routeRoot = query.from(RouteEntity.class);
            return cb.and(
                    cb.equal(root.get("routeId"), routeRoot.get("id")),
                    cb.equal(routeRoot.get("originProvinceId"), provinceId)
            );
        };
    }

    public static Specification<TripAggregate> destinationNameContainsIgnoreCase(String destinationName) {
        String v = normalize(destinationName);
        return (root, query, cb) -> {
            if (v.isBlank()) return cb.conjunction();
            Root<RouteEntity> routeRoot = query.from(RouteEntity.class);
            return cb.and(
                    cb.equal(root.get("routeId"), routeRoot.get("id")),
                    cb.like(cb.lower(routeRoot.get("destinationName")), "%" + v + "%")
            );
        };
    }

    public static Specification<TripAggregate> hasDestinationProvinceId(String provinceId) {
        return (root, query, cb) -> {
            if (provinceId == null || provinceId.isBlank()) return cb.conjunction();
            Root<RouteEntity> routeRoot = query.from(RouteEntity.class);
            return cb.and(
                    cb.equal(root.get("routeId"), routeRoot.get("id")),
                    cb.equal(routeRoot.get("destinationProvinceId"), provinceId)
            );
        };
    }

    public static Specification<TripAggregate> assignedStatus(TripStatus status) {
        return (root, query, cb) -> {
            if(status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<TripAggregate> hasMerchantId(String merchantId) {
        return (root, query, cb) -> {
            if (merchantId == null || merchantId.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("merchantId"), merchantId);
        };
    }


    public static Specification<TripAggregate> hasMerchantIds(List<String> merchantIds) {
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
    private static String normalize(String message) {
        return message == null ? "" : message.trim().toLowerCase();
    }
}
