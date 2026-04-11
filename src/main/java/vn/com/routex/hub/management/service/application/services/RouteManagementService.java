package vn.com.routex.hub.management.service.application.services;


import vn.com.routex.hub.management.service.application.command.route.FetchRoutesQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteResult;

public interface RouteManagementService {
    SearchRouteResult searchRoute(SearchRouteQuery query);

    FetchRoutesResult fetchRoutes(FetchRoutesQuery query);
}
