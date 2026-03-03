package vn.com.routex.hub.user.service.interfaces.models.route;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateRouteResponse extends BaseResponse {
    private CreateRouteResponseData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateRouteResponseData {
        private String id;
        private String creator;
        private String routeCode;
        private String origin;
        private String destination;
        private String plannedStartTime;
        private String plannedEndTime;
        private String status;
        private List<CreateRouteRequest.RouteStopPoints> stopPoints;
    }
}
