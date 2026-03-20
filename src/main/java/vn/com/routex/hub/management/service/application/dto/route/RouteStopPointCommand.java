package vn.com.routex.hub.management.service.application.dto.route;

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
public class RouteStopPointCommand {
    private String stopOrder;
    private String plannedArrivalTime;
    private String plannedDepartureTime;
    private String note;
}
