package vn.com.routex.hub.management.service.application.command.route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchRouteResult {
    private String id;
    private String creator;
    private String pickupBranch;
    private String routeCode;
    private String origin;
    private String destination;
    private OffsetDateTime plannedStartTime;
    private OffsetDateTime plannedEndTime;
    private OffsetDateTime actualStartTime;
    private OffsetDateTime actualEndTime;
    private String status;
    private Long availableSeats;
    private String vehicleId;
    private String vehiclePlate;
    private Boolean hasFloor;
    private OffsetDateTime assignedAt;
    private List<OperationPointResult> operationPoints;
}
