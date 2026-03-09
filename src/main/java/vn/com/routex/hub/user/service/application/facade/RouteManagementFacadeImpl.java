package vn.com.routex.hub.user.service.application.facade;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.application.services.RouteManagementService;
import vn.com.routex.hub.user.service.infrastructure.utils.HttpResponseUtil;
import vn.com.routex.hub.user.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.user.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.user.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.route.SearchRouteResponse;

@Component
@RequiredArgsConstructor
public class RouteManagementFacadeImpl implements RouteManagementFacade {

    private final RouteManagementService routeManagementService;

    @Override
    public ResponseEntity<CreateRouteResponse> createRoute(CreateRouteRequest request) {
        CreateRouteResponse response = routeManagementService.createRoute(request);
        return HttpResponseUtil.buildResponse(request, response);
    }

    @Override
    public ResponseEntity<AssignRouteResponse> assignRoute(AssignRouteRequest request) {
        AssignRouteResponse response = routeManagementService.assignRoute(request);
        return HttpResponseUtil.buildResponse(request, response);
    }

    @Override
    public ResponseEntity<SearchRouteResponse> searchRoute(SearchRouteRequest request) {
        SearchRouteResponse response = routeManagementService.searchRoute(request);
        return HttpResponseUtil.buildResponse(request, response);
    }
}
