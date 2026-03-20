package vn.com.routex.hub.management.service.application.dto.route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateRouteResult {
    private String id;
    private String creator;
    private String pickupBranch;
    private String routeCode;
    private String origin;
    private String destination;
    private String plannedStartTime;
    private String plannedEndTime;
    private String status;
    private List<RouteStopPointCommand> stopPoints;
}
