package vn.com.routex.hub.user.service.domain.assignment;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROUTE_ASSIGNMENT")
public class RouteAssignment {
    @Id
    private String id;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "VEHICLE_ID")
    private String vehicleId;

    @Column(name = "ASSIGNED_AT")
    private OffsetDateTime assignedAt;

    @Column(name = "UNASSIGNED_AT")
    private OffsetDateTime unAssignedAt;

    @Column(name = "STATUS")
    private RouteAssignmentStatus status;

}
