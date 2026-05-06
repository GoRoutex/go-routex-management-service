package vn.com.routex.hub.management.service.interfaces.models.trip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.trip.TripStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchTripResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchTripResponse extends BaseResponse<FetchTripResponse.FetchTripResponsePage> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchTripResponsePage {
        private List<FetchTripResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchTripResponseData {
        private String id;
        private String creator;
        private String pickupBranch;
        private String tripCode;
        private String originCode;
        private String originName;
        private String destinationCode;
        private String destinationName;
        private String originProvinceId;
        private String destinationProvinceId;
        private String originDepartmentId;
        private String destinationDepartmentId;
        private OffsetDateTime departureTime;
        private String rawDepartureDate;
        private String rawDepartureTime;
        private String rawArrivalTime;
        private TripStatus status;
        private String vehicleId;
        private String vehiclePlate;
        private Boolean hasFloor;
        private OffsetDateTime assignedAt;
        private List<SearchTripResponse.SearchRoutePoints> routePoints;
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
