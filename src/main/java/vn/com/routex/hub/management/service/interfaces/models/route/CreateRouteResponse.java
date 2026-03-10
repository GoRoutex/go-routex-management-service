package vn.com.routex.hub.management.service.interfaces.models.route;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateRouteResponse extends BaseResponse<CreateRouteResponse.CreateRouteResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateRouteResponseData {
        private String id;
        private String creator;
        private String pickupBranch;
        private String routeCode;
        private String origin;
        private String destination;
        private String plannedStartTime;
        private String plannedEndTime;
        private String status;
        private List<CreateRouteRequest.RouteStopPoints> stopPoints;
    }
}
