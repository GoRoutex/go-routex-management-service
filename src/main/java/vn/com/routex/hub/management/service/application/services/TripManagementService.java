package vn.com.routex.hub.management.service.application.services;


import vn.com.routex.hub.management.service.application.command.route.FetchTripQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchTripResult;
import vn.com.routex.hub.management.service.application.command.route.FetchTripsQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchTripsResult;
import vn.com.routex.hub.management.service.application.command.route.SearchTripQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchTripResult;

public interface TripManagementService {
    SearchTripResult searchTrip(SearchTripQuery query);

    FetchTripResult fetchTripDetail(FetchTripQuery query);

    FetchTripsResult fetchTrips(FetchTripsQuery query);
}
