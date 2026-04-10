package vn.com.routex.hub.management.service.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.route.AssignRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.AssignRouteResult;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteResult;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteResult;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesResult;
import vn.com.routex.hub.management.service.application.command.route.RoutePointCommand;
import vn.com.routex.hub.management.service.application.command.route.UpdateRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.UpdateRouteCommand.UpdateRoutePointCommand;
import vn.com.routex.hub.management.service.application.command.route.UpdateRouteResult;
import vn.com.routex.hub.management.service.application.services.RouteManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.mapper.RouteResponseMapper;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.DeleteRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.DeleteRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.FetchRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.UpdateRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.UpdateRouteResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ASSIGNMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ROUTE_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;


@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH + ROUTE_SERVICE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('route:management') or hasRole('ADMIN')")
public class RouteManagementController {


    private final RouteManagementService routeManagementService;
    private final ApiResultFactory apiResultFactory;
    private final RouteResponseMapper routeResponseMapper;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(UPDATE_PATH)
    public ResponseEntity<UpdateRouteResponse> updateRoute(@Valid @RequestBody UpdateRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[UPDATE-ROUTE] Update Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        List<UpdateRoutePointCommand> routePointCommandList = null;
        if (request.getData().getRoutePoints() != null) {
            routePointCommandList = request.getData().getRoutePoints().stream().map(
                    point -> UpdateRoutePointCommand.builder()
                            .id(point.getId())
                            .operationOrder(point.getOperationOrder())
                            .plannedArrivalTime(OffsetDateTime.parse(point.getPlannedArrivalTime()))
                            .plannedDepartureTime(OffsetDateTime.parse(point.getPlannedDepartureTime()))
                            .note(point.getNote())
                            .build()
            ).toList();
        }

        UpdateRouteResult result = routeManagementService.updateRoute(UpdateRouteCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .routeId(request.getRouteId())
                .creator(request.getCreator())
                .pickupBranch(request.getData().getPickupBranch())
                .origin(request.getData().getOrigin())
                .destination(request.getData().getDestination())
                .plannedStartTime(OffsetDateTime.parse(request.getData().getPlannedStartTime()))
                .plannedEndTime(OffsetDateTime.parse(request.getData().getPlannedEndTime()))
                .actualStartTime(OffsetDateTime.parse(request.getData().getActualStartTime()))
                .actualEndTime(OffsetDateTime.parse(request.getData().getActualEndTime()))
                .status(request.getData().getStatus())
                .routePoints(routePointCommandList)
                .build());

        UpdateRouteResponse response = UpdateRouteResponse.builder()
                .routeId(result.routeId())
                .creator(result.creator())
                .data(UpdateRouteResponse.UpdateRouteResponseData.builder()
                        .pickupBranch(result.pickupBranch())
                        .origin(result.origin())
                        .destination(result.destination())
                        .plannedStartTime(result.plannedStartTime())
                        .plannedEndTime(result.plannedEndTime())
                        .actualStartTime(result.actualStartTime())
                        .actualEndTime(result.actualEndTime())
                        .status(result.status())
                        .routePoints(result.routePoints() == null ? null : result.routePoints().stream().map(
                                point -> UpdateRouteResponse.UpdateRoutePointResponse.builder()
                                        .id(point.id())
                                        .operationOrder(point.operationOrder())
                                        .plannedArrivalTime(point.plannedArrivalTime())
                                        .plannedDepartureTime(point.plannedDepartureTime())
                                        .note(point.note())
                                        .build()
                        ).collect(Collectors.toList()))
                        .build())
                .build();

        sLog.info("[UPDATE-ROUTE] Update Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(CREATE_PATH)
    public ResponseEntity<CreateRouteResponse> createRoute(@Valid @RequestBody CreateRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[CREATE-ROUTE] Create Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        List<RoutePointCommand> routePointCommands = new ArrayList<>();
        if (request.getData().getOperationPoints() != null) {
            routePointCommands = request.getData().getOperationPoints().stream()
                    .map(point -> RoutePointCommand.builder()
                            .operationOrder(point.getOperationOrder())
                            .plannedArrivalTime(point.getPlannedArrivalTime())
                            .plannedDepartureTime(point.getPlannedDepartureTime())
                            .note(point.getNote())
                            .operationPointId(point.getOperationPointId())
                            .stopName(point.getStopName())
                            .stopAddress(point.getStopAddress())
                            .stopCity(point.getStopCity())
                            .stopLatitude(point.getStopLatitude())
                            .stopLongitude(point.getStopLongitude())
                            .build())
                    .toList();
        }

        CreateRouteResult result = routeManagementService.createRoute(CreateRouteCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .creator(request.getData().getCreator())
                .pickupBranch(request.getData().getPickupBranch())
                .origin(request.getData().getOrigin())
                .destination(request.getData().getDestination())
                .plannedStartTime(request.getData().getPlannedStartTime())
                .plannedEndTime(request.getData().getPlannedEndTime())
                .routePoints(routePointCommands)
                .build());


        List<CreateRouteRequest.RoutePoints> routePointResponses = new ArrayList<>();
        if (result.routePoints() != null) {
            routePointResponses = result.routePoints().stream()
                    .map(point -> {
                        CreateRouteRequest.RoutePoints rp = new CreateRouteRequest.RoutePoints();
                        rp.setOperationOrder(point.operationOrder());
                        rp.setPlannedArrivalTime(point.plannedArrivalTime());
                        rp.setPlannedDepartureTime(point.plannedDepartureTime());
                        rp.setNote(point.note());
                        rp.setOperationPointId(point.operationPointId());
                        rp.setStopName(point.stopName());
                        rp.setStopAddress(point.stopAddress());
                        rp.setStopCity(point.stopCity());
                        rp.setStopLatitude(point.stopLatitude());
                        rp.setStopLongitude(point.stopLongitude());
                        return rp;
                    })
                    .toList();
        }

        CreateRouteResponse response = CreateRouteResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(CreateRouteResponse.CreateRouteResponseData.builder()
                        .id(result.id())
                        .creator(result.creator())
                        .pickupBranch(result.pickupBranch())
                        .routeCode(result.routeCode())
                        .origin(result.origin())
                        .destination(result.destination())
                        .plannedStartTime(result.plannedStartTime())
                        .plannedEndTime(result.plannedEndTime())
                        .status(result.status())
                        .routePoints(routePointResponses)
                        .build())
                .build();

        sLog.info("[CREATE-ROUTE] Create Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(ASSIGNMENT_PATH)
    public ResponseEntity<AssignRouteResponse> assignRoute(@Valid @RequestBody AssignRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[ASSIGN-ROUTE] Assign Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        AssignRouteResult result = routeManagementService.assignRoute(AssignRouteCommand.builder()
                .merchantId(merchantId)
                .creator(request.getData().getCreator())
                .routeId(request.getData().getRouteId())
                .vehicleId(request.getData().getVehicleId())
                .context(HttpUtils.toContext(request, merchantId))
                .build());

        AssignRouteResponse response = AssignRouteResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(AssignRouteResponse.AssignRouteResponseData.builder()
                        .creator(result.creator())
                        .routeId(result.routeId())
                        .vehicleId(result.vehicleId())
                        .assignedAt(result.assignedAt())
                        .status(result.status())
                        .build())
                .build();

        sLog.info("[ASSIGN-ROUTE] Assign Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(DELETE_PATH)
    public ResponseEntity<DeleteRouteResponse> deleteRoute(@Valid @RequestBody DeleteRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[DELETE-ROUTE] Delete Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        DeleteRouteResult result = routeManagementService.deleteRoute(DeleteRouteCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .creator(request.getData().getCreator())
                .routeId(request.getData().getRouteId())
                .merchantId(merchantId)
                .build());


        DeleteRouteResponse response = DeleteRouteResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(DeleteRouteResponse.DeleteRouteResponseData.builder()
                        .creator(result.creator())
                        .routeId(result.routeId())
                        .routeCode(result.routeCode())
                        .status(result.status())
                        .updatedAt(result.updatedAt())
                        .build())
                .build();

        sLog.info("[DELETE-ROUTE] Delete Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(FETCH_PATH)
    public ResponseEntity<FetchRouteResponse> fetchRoutes(
            HttpServletRequest servletRequest,
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false) String merchantName,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String tokenMerchantId = ApiRequestUtils.getMerchantId(servletRequest);
        String finalMerchantId = tokenMerchantId;
        String finalMerchantName = merchantName;

        boolean isAdmin = servletRequest.isUserInRole("ADMIN") || servletRequest.isUserInRole("STAFF");

        if (isAdmin) {
            if (merchantId != null) finalMerchantId = merchantId;
        } else {
            // Non-admin MUST have a merchantId in token
            if (tokenMerchantId == null) {
                return ResponseEntity.status(403).build();
            }
            finalMerchantId = tokenMerchantId;
            finalMerchantName = null;
        }

        FetchRoutesResult result = routeManagementService.fetchRoutes(FetchRoutesQuery.builder()
                .context(HttpUtils.toContext(baseRequest, finalMerchantId))
                .pageSize(String.valueOf(pageSize))
                .pageNumber(String.valueOf(pageNumber))
                .merchantId(finalMerchantId)
                .merchantName(finalMerchantName)
                .build());


        FetchRouteResponse response = FetchRouteResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(FetchRouteResponse.FetchRouteResponsePage.builder()
                        .items(result.items().stream()
                                .map(routeResponseMapper::toFetchRouteResponseData)
                                .toList())
                        .pagination(FetchRouteResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }
}

