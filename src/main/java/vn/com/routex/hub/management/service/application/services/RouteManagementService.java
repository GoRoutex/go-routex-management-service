package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteResponse;

public interface RouteManagementService {

    CreateRouteResponse createRoute(CreateRouteRequest request);

    AssignRouteResponse assignRoute(AssignRouteRequest request);

    SearchRouteResponse searchRoute(SearchRouteRequest request);
}
