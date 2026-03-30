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
public class OperationPointResult {
    private String id;
    private String operationOrder;
    private String routeId;
    private String plannedArrivalTime;
    private String plannedDepartureTime;
    private String note;
}
