package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.management.service.application.command.route.AssignRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.AssignRouteResult;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteResult;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteResult;
import vn.com.routex.hub.management.service.application.command.route.FetchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.OperationPointCommand;
import vn.com.routex.hub.management.service.application.command.route.OperationPointResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteItemResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteResult;
import vn.com.routex.hub.management.service.application.services.RouteManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpResponseUtil;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.DeleteRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.DeleteRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.FetchRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.FetchRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteResponse;

import java.util.ArrayList;
import java.util.List;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ASSIGNMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ROUTE_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH + ROUTE_SERVICE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('route:management') or hasRole('ADMIN')")
public class RouteManagementController {

    private final RouteManagementService routeManagementService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(CREATE_PATH)
    public ResponseEntity<CreateRouteResponse> createRoute(@Valid @RequestBody CreateRouteRequest request) {
        List<OperationPointCommand> operationPointCommands = new ArrayList<>();
        if (request.getData().getOperationPoints() != null) {
            operationPointCommands = request.getData().getOperationPoints().stream()
                    .map(point -> {
                        OperationPointCommand command = new OperationPointCommand();
                        command.setOperationOrder(point.getOperationOrder());
                        command.setPlannedArrivalTime(point.getPlannedArrivalTime());
                        command.setPlannedDepartureTime(point.getPlannedDepartureTime());
                        command.setNote(point.getNote());
                        return command;
                    })
                    .toList();
        }

        CreateRouteResult result = routeManagementService.createRoute(CreateRouteCommand.builder()
                .creator(request.getData().getCreator())
                .pickupBranch(request.getData().getPickupBranch())
                .origin(request.getData().getOrigin())
                .destination(request.getData().getDestination())
                .plannedStartTime(request.getData().getPlannedStartTime())
                .plannedEndTime(request.getData().getPlannedEndTime())
                .operationPoints(operationPointCommands)
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        List<CreateRouteRequest.OperationPoints> operationPointResponses = new ArrayList<>();
        if (result.getOperationPoints() != null) {
            operationPointResponses = result.getOperationPoints().stream()
                    .map(point -> {
                        CreateRouteRequest.OperationPoints op = new CreateRouteRequest.OperationPoints();
                        op.setOperationOrder(point.getOperationOrder());
                        op.setPlannedArrivalTime(point.getPlannedArrivalTime());
                        op.setPlannedDepartureTime(point.getPlannedDepartureTime());
                        op.setNote(point.getNote());
                        return op;
                    })
                    .toList();
        }

        CreateRouteResponse response = CreateRouteResponse.builder()
                .result(successResult())
                .data(CreateRouteResponse.CreateRouteResponseData.builder()
                        .id(result.getId())
                        .creator(result.getCreator())
                        .pickupBranch(result.getPickupBranch())
                        .routeCode(result.getRouteCode())
                        .origin(result.getOrigin())
                        .destination(result.getDestination())
                        .plannedStartTime(result.getPlannedStartTime())
                        .plannedEndTime(result.getPlannedEndTime())
                        .status(result.getStatus())
                        .operationPoints(operationPointResponses)
                        .build())
                .build();

        return HttpResponseUtil.buildResponse(request, response);
    }

    @PostMapping(ASSIGNMENT_PATH)
    public ResponseEntity<AssignRouteResponse> assignRoute(@Valid @RequestBody AssignRouteRequest request) {
        AssignRouteResult result = routeManagementService.assignRoute(AssignRouteCommand.builder()
                .creator(request.getData().getCreator())
                .routeId(request.getData().getRouteId())
                .vehicleId(request.getData().getVehicleId())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        AssignRouteResponse response = AssignRouteResponse.builder()
                .result(successResult())
                .data(AssignRouteResponse.AssignRouteResponseData.builder()
                        .creator(result.getCreator())
                        .routeId(result.getRouteId())
                        .vehicleId(result.getVehicleId())
                        .assignedAt(result.getAssignedAt())
                        .status(result.getStatus())
                        .build())
                .build();

        return HttpResponseUtil.buildResponse(request, response);
    }


    @PostMapping(SEARCH_PATH)
    public ResponseEntity<SearchRouteResponse> searchRoute(@Valid @RequestBody SearchRouteRequest request) {
        SearchRouteResult result = routeManagementService.searchRoute(SearchRouteQuery.builder()
                .origin(request.getData().getOrigin())
                .destination(request.getData().getDestination())
                .departureDate(request.getData().getDepartureDate())
                .seat(request.getData().getSeat())
                .fromTime(request.getData().getFromTime())
                .toTime(request.getData().getToTime())
                .pageSize(request.getData().getPageSize())
                .pageNumber(request.getData().getPageNumber())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        SearchRouteResponse response = SearchRouteResponse.builder()
                .result(successResult())
                .data(result.getData().stream()
                        .map(this::toSearchRouteResponseData)
                        .toList())
                .build();

        return HttpResponseUtil.buildResponse(request, response);
    }

    @PostMapping(FETCH_PATH)
    public ResponseEntity<FetchRouteResponse> fetchRoute(@Valid @RequestBody FetchRouteRequest request) {
        FetchRouteResult result = routeManagementService.fetchRoute(FetchRouteQuery.builder()
                .routeId(request.getData().getRouteId())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        FetchRouteResponse response = FetchRouteResponse.builder()
                .result(successResult())
                .data(FetchRouteResponse.FetchRouteResponseData.builder()
                        .id(result.getId())
                        .creator(result.getCreator())
                        .pickupBranch(result.getPickupBranch())
                        .routeCode(result.getRouteCode())
                        .origin(result.getOrigin())
                        .destination(result.getDestination())
                        .plannedStartTime(result.getPlannedStartTime())
                        .plannedEndTime(result.getPlannedEndTime())
                        .actualStartTime(result.getActualStartTime())
                        .actualEndTime(result.getActualEndTime())
                        .status(result.getStatus())
                        .availableSeats(result.getAvailableSeats())
                        .vehicleId(result.getVehicleId())
                        .vehiclePlate(result.getVehiclePlate())
                        .hasFloor(result.getHasFloor())
                        .assignedAt(result.getAssignedAt())
                        .operationPoints(result.getOperationPoints().stream()
                                .map(this::toSearchOperationPoint)
                                .toList())
                        .build())
                .build();

        return HttpResponseUtil.buildResponse(request, response);
    }

    @PostMapping(DELETE_PATH)
    public ResponseEntity<DeleteRouteResponse> deleteRoute(@Valid @RequestBody DeleteRouteRequest request) {
        DeleteRouteResult result = routeManagementService.deleteRoute(DeleteRouteCommand.builder()
                .creator(request.getData().getCreator())
                .routeId(request.getData().getRouteId())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        DeleteRouteResponse response = DeleteRouteResponse.builder()
                .result(successResult())
                .data(DeleteRouteResponse.DeleteRouteResponseData.builder()
                        .creator(result.getCreator())
                        .routeId(result.getRouteId())
                        .routeCode(result.getRouteCode())
                        .status(result.getStatus())
                        .updatedAt(result.getUpdatedAt())
                        .build())
                .build();

        return HttpResponseUtil.buildResponse(request, response);
    }

    private SearchRouteResponse.SearchRouteResponseData toSearchRouteResponseData(SearchRouteItemResult item) {
        return SearchRouteResponse.SearchRouteResponseData.builder()
                .id(item.getId())
                .pickupBranch(item.getPickupBranch())
                .origin(item.getOrigin())
                .destination(item.getDestination())
                .availableSeats(item.getAvailableSeats())
                .plannedStartTime(item.getPlannedStartTime())
                .plannedEndTime(item.getPlannedEndTime())
                .vehiclePlate(item.getVehiclePlate())
                .hasFloor(item.isHasFloor())
                .routeCode(item.getRouteCode())
                .operationPoints(item.getOperationPoints().stream()
                        .map(this::toSearchOperationPoint)
                        .toList())
                .build();
    }

    private SearchRouteResponse.SearchOperationPoints toSearchOperationPoint(OperationPointResult point) {
        return SearchRouteResponse.SearchOperationPoints.builder()
                .id(point.getId())
                .operationOrder(point.getOperationOrder())
                .routeId(point.getRouteId())
                .plannedArrivalTime(point.getPlannedArrivalTime())
                .plannedDepartureTime(point.getPlannedDepartureTime())
                .note(point.getNote())
                .build();
    }

    private ApiResult successResult() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }

}
