package vn.com.routex.hub.user.service.application.facade;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.application.services.RouteManagementService;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.user.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.user.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.route.SearchRouteResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class RouteManagementFacadeImpl implements RouteManagementFacade {

    private final RouteManagementService routeManagementService;

    @Override
    public ResponseEntity<CreateRouteResponse> createRoute(CreateRouteRequest request) {
        CreateRouteResponse response = routeManagementService.createRoute(request);
        if(response == null) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));
        }
        response.setRequestId(request.getRequestId());
        response.setRequestDateTime(request.getRequestDateTime());
        response.setChannel(request.getChannel());
        if(response.getData() == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssignRouteResponse> assignRoute(AssignRouteRequest request) {
        AssignRouteResponse response = routeManagementService.assignRoute(request);
        if(response == null) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));
        }
        response.setRequestId(request.getRequestId());
        response.setRequestDateTime(request.getRequestDateTime());
        response.setChannel(request.getChannel());
        if(response.getData() == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SearchRouteResponse> searchRoute(SearchRouteRequest request) {
        SearchRouteResponse response = routeManagementService.searchRoute(request);
        if(response == null) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(),request.getChannel(),
                    ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));
        }
        response.setRequestId(request.getRequestId());
        response.setRequestDateTime(request.getRequestDateTime());
        response.setChannel(request.getChannel());
        if(response.getData() == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
