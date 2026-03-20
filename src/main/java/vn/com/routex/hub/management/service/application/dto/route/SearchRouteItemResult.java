package vn.com.routex.hub.management.service.application.dto.route;

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
public class SearchRouteItemResult {
    private String id;
    private String pickupBranch;
    private String origin;
    private String destination;
    private Long availableSeats;
    private OffsetDateTime plannedStartTime;
    private OffsetDateTime plannedEndTime;
    private String vehiclePlate;
    private boolean hasFloor;
    private String routeCode;
    private List<RouteStopPointResult> stopPoints;
}
