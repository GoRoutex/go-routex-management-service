package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.management.service.application.facade.RouteManagementFacade;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ASSIGNMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ROUTE_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH + ROUTE_SERVICE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('route:management') and hasRole('ADMIN')")
public class RouteManagementController {

    private final RouteManagementFacade routeManagementFacade;

    @PostMapping(CREATE_PATH)
    public ResponseEntity<CreateRouteResponse> createRoute(@Valid @RequestBody CreateRouteRequest request) {
        return routeManagementFacade.createRoute(request);
    }

    @PostMapping(ASSIGNMENT_PATH)
    public ResponseEntity<AssignRouteResponse> assignRoute(@Valid @RequestBody AssignRouteRequest request) {
        return routeManagementFacade.assignRoute(request);
    }


    @PostMapping(SEARCH_PATH)
    public ResponseEntity<SearchRouteResponse> searchRoute(@Valid @RequestBody SearchRouteRequest request) {
        return routeManagementFacade.searchRoute(request);
    }

}
