package vn.com.routex.hub.user.service.interfaces.models.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchRouteResponse extends BaseResponse {
    private List<SearchRouteResponseData> data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchRouteResponseData {
        private String id;
        private String origin;
        private String destination;
        private OffsetDateTime plannedStartTime;
        private OffsetDateTime plannedEndTime;
        private String routeCode;
        private List<SearchStopPoints> stopPoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchStopPoints {
        private String id;
        private String stopOrder;
        private String routeId;
        private String plannedArrivalTime;
        private String plannedDepartureTime;
        private String note;
    }
}
