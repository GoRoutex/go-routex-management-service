package vn.com.routex.hub.management.service.application.facade;

import org.springframework.http.ResponseEntity;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteResponse;

public interface RouteManagementFacade {

    ResponseEntity<CreateRouteResponse> createRoute(CreateRouteRequest request);

    ResponseEntity<AssignRouteResponse> assignRoute(AssignRouteRequest request);

    ResponseEntity<SearchRouteResponse> searchRoute(SearchRouteRequest request);
}
