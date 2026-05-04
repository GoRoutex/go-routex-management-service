package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripFetchView;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripSearchView;

import java.time.OffsetDateTime;
import java.util.List;

public interface TripQueryPort {
    List<TripSearchView> searchAssignedTrips(
            String merchantId,
            String originName,
            String destinationName,
            int pageNumber,
            int pageSize
    );

    PagedResult<TripFetchView> fetchTrips(String merchantId, String merchantName, int pageNumber, int pageSize);

}
