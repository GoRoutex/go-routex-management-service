package vn.com.routex.hub.management.service.interfaces.models.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SearchRouteResponse extends BaseResponse<List<SearchRouteResponse.SearchRouteResponseData>> {
    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchRouteResponseData {
        private String id;
        private String merchantId;
        private String merchantName;
        private String pickupBranch;
        private String origin;
        private String destination;
        private Long availableSeats;
        private OffsetDateTime plannedStartTime;
        private OffsetDateTime plannedEndTime;
        private String vehiclePlate;
        private boolean hasFloor;
        private String routeCode;
        private List<SearchRoutePoints> routePoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchRoutePoints {
        private String id;
        private String operationOrder;
        private String routeId;
        private String plannedArrivalTime;
        private String plannedDepartureTime;
        private String note;
        private String operationPointId;
        private String stopName;
        private String stopAddress;
        private String stopCity;
        private Double stopLatitude;
        private Double stopLongitude;
    }
}
