package vn.com.routex.hub.management.service.application.command.route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AssignRouteResult {
    private String creator;
    private String routeId;
    private String vehicleId;
    private String driverId;
    private String assignedAt;
    private String status;
}
