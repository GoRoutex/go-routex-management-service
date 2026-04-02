package vn.com.routex.hub.management.service.interfaces.models.route;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateRouteRequest extends BaseRequest {

    private String routeId;
    private String creator;

    @Valid
    @NotNull
    private UpdateRouteRequestData data;

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateRouteRequestData {
        private String pickupBranch;
        private String origin;
        private String destination;
        private String plannedStartTime;
        private String plannedEndTime;
        private String actualStartTime;
        private String actualEndTime;
        private RouteStatus status;
        private List<UpdateRoutePointRequest> routePoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @SuperBuilder
    @NoArgsConstructor
    public static class UpdateRoutePointRequest {
        private String id;
        private String operationOrder;
        private String routeId;
        private String plannedArrivalTime;
        private String plannedDepartureTime;
        private String note;
    }
}
