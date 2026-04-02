package vn.com.routex.hub.management.service.interfaces.models.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateRouteResponse extends BaseResponse<UpdateRouteResponse.UpdateRouteResponseData> {

    private String routeId;
    private String creator;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateRouteResponseData {
        private String pickupBranch;
        private String origin;
        private String destination;
        private OffsetDateTime plannedStartTime;
        private OffsetDateTime plannedEndTime;
        private OffsetDateTime actualStartTime;
        private OffsetDateTime actualEndTime;
        private RouteStatus status;
        private List<UpdateOperationPointResponse> operationPoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateOperationPointResponse {
        private String id;
        private String operationOrder;
        private String routeId;
        private OffsetDateTime plannedArrivalTime;
        private OffsetDateTime plannedDepartureTime;
        private String note;
    }
}

