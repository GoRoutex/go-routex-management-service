package vn.com.routex.hub.management.service.application.command.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RouteStopPointResult {
    private String id;
    private String stopOrder;
    private String routeId;
    private String plannedArrivalTime;
    private String plannedDepartureTime;
    private String note;
}
