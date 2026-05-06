package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripFetchView;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripSearchView;

import java.util.List;

public interface TripQueryPort {
    List<TripSearchView> searchAssignedTrips(
            String merchantId,
            String originName,
            String destinationName,
            String originProvinceId,
            String destinationProvinceId,
            int pageNumber,
            int pageSize
    );

    PagedResult<TripFetchView> fetchTrips(String merchantId, List<String> merchantIds, int pageNumber, int pageSize);

}
