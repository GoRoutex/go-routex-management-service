package vn.com.routex.hub.management.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteAggregate extends AbstractAuditingEntity {
    private String id;
    private String creator;
    private String merchantId;
    private String originName;
    private String destinationName;
    private String originCode;
    private String destinationCode;
    private String originProvinceId;
    private String destinationProvinceId;
    private String originDepartmentId;
    private String destinationDepartmentId;
    private String originDepartmentName;
    private String destinationDepartmentName;
    private Long duration;
    private Long distance;
    private RouteStatus status;
    private List<RouteStopPlan> stopPlans;

    public static RouteAggregate plan(
            String id,
            String creator,
            String merchantId,
            String originCode,
            String destinationCode,
            String originProvinceId,
            String destinationProvinceId,
            String originDepartmentId,
            String destinationDepartmentId,
            String originDepartmentName,
            String destinationDepartmentName,
            String originName,
            String destinationName,
            Long duration,
            Long distance,
            OffsetDateTime createdAt,
            List<RouteStopPlan> stopPlans
    ) {
        return RouteAggregate.builder()
                .id(id)
                .creator(creator)
                .merchantId(merchantId)
                .originCode(originCode)
                .destinationCode(destinationCode)
                .originProvinceId(originProvinceId)
                .destinationProvinceId(destinationProvinceId)
                .originDepartmentId(originDepartmentId)
                .destinationDepartmentId(destinationDepartmentId)
                .originDepartmentName(originDepartmentName)
                .destinationDepartmentName(destinationDepartmentName)
                .originName(originName)
                .destinationName(destinationName)
                .duration(duration)
                .distance(distance)
                .status(RouteStatus.ACTIVE)
                .createdAt(createdAt)
                .createdBy(creator)
                .stopPlans(stopPlans == null ? new ArrayList<>() : new ArrayList<>(stopPlans))
                .build();
    }

    public void cancel(String creator, OffsetDateTime now) {
        this.status = RouteStatus.SUSPENDED;
        this.setUpdatedBy(creator);
        this.setUpdatedAt(now);
    }
}

