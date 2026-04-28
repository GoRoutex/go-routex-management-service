package vn.com.routex.hub.management.service.interfaces.models.route;

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
public class FetchRouteResponse extends BaseResponse<FetchRouteResponse.FetchRouteResponsePage> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchRouteResponsePage {
        private List<FetchRouteResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchRouteResponseData {
        private String id;
        private String creator;
        private String pickupBranch;
        private String routeCode;
        private String origin;
        private String destination;
        private OffsetDateTime plannedStartTime;
        private OffsetDateTime plannedEndTime;
        private OffsetDateTime actualStartTime;
        private OffsetDateTime actualEndTime;
        private String status;
        private String vehicleId;
        private String vehiclePlate;
        private Boolean hasFloor;
        private OffsetDateTime assignedAt;
        private List<SearchRouteResponse.SearchRoutePoints> routePoints;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
