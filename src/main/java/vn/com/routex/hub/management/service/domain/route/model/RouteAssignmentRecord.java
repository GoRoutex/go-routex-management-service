package vn.com.routex.hub.management.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignmentStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteAssignmentRecord {
    private String id;
    private String routeId;
    private String creator;
    private String vehicleId;
    private OffsetDateTime assignedAt;
    private OffsetDateTime unAssignedAt;
    private RouteAssignmentStatus status;
    private OffsetDateTime updatedAt;
    private String updatedBy;

    public static RouteAssignmentRecord assign(
            String id,
            String routeId,
            String creator,
            String vehicleId,
            OffsetDateTime assignedAt
    ) {
        return RouteAssignmentRecord.builder()
                .id(id)
                .routeId(routeId)
                .creator(creator)
                .vehicleId(vehicleId)
                .assignedAt(assignedAt)
                .status(RouteAssignmentStatus.ASSIGNED)
                .build();
    }

    public void cancel(String actor, OffsetDateTime at) {
        this.status = RouteAssignmentStatus.CANCELED;
        this.unAssignedAt = at;
        this.updatedAt = at;
        this.updatedBy = actor;
    }
}
