package vn.com.routex.hub.management.service.application.command.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateRouteCommand {
    private String creator;
    private String pickupBranch;
    private String origin;
    private String destination;
    private String plannedStartTime;
    private String plannedEndTime;
    private List<RouteStopPointCommand> stopPoints;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
