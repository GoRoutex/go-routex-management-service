package vn.com.routex.hub.management.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteStopPlan {
    private String id;
    private String routeId;
    private String creator;
    private int stopOrder;
    private OffsetDateTime plannedArrivalTime;
    private OffsetDateTime plannedDepartureTime;
    private String note;
    private OffsetDateTime createdAt;
    private String createdBy;
}
