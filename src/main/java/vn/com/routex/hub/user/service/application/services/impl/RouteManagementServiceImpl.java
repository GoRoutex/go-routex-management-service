package vn.com.routex.hub.user.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.services.RouteManagementService;
import vn.com.routex.hub.user.service.domain.assignment.RouteAssignment;
import vn.com.routex.hub.user.service.domain.assignment.RouteAssignmentRepository;
import vn.com.routex.hub.user.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.user.service.domain.route.Route;
import vn.com.routex.hub.user.service.domain.route.RouteRepository;
import vn.com.routex.hub.user.service.domain.route.RouteStatus;
import vn.com.routex.hub.user.service.domain.stoppoint.RouteStop;
import vn.com.routex.hub.user.service.domain.stoppoint.RouteStopRepository;
import vn.com.routex.hub.user.service.domain.vehicle.VehicleRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.user.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.user.service.interfaces.models.route.CreateRouteResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ROUTE_ASSIGNMENT;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PLANNED_TIME;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_START_TIME;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_STOP_ORDER;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class RouteManagementServiceImpl implements RouteManagementService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final RouteAssignmentRepository routeAssignmentRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public CreateRouteResponse createRoute(CreateRouteRequest request) {
        String origin = request.getData().getOrigin().trim();
        String destination = request.getData().getDestination().trim();
        String routeCode = routeRepository.generateRouteCode(origin, destination);

        OffsetDateTime plannedStartTime = OffsetDateTime.parse(request.getData().getPlannedStartTime());
        OffsetDateTime plannedEndTime = OffsetDateTime.parse(request.getData().getPlannedEndTime());

        if(!plannedStartTime.isBefore(plannedEndTime)) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_START_TIME));
        }

        List<CreateRouteRequest.RouteStopPoints> stopPoints =
                Optional.ofNullable(request.getData().getStopPoints())
                        .orElseGet(List::of);

        // Validate stop points
        validateStopPoints(request);

        Route newRoute = Route
                .builder()
                .id(UUID.randomUUID().toString())
                .routeCode(routeCode)
                .origin(origin)
                .creator(request.getData().getCreator())
                .destination(destination)
                .plannedStartTime(plannedStartTime)
                .plannedEndTime(plannedEndTime)
                .status(RouteStatus.PLANNED)
                .createdAt(LocalDateTime.now())
                .createdBy(request.getData().getCreator())
                .build();


        routeRepository.save(newRoute);

        List<RouteStop> routeStopList = stopPoints.stream()
                .map(point -> {
                    OffsetDateTime arrival = OffsetDateTime.parse(point.getPlannedArrivalTime());
                    OffsetDateTime departure = OffsetDateTime.parse(point.getPlannedDepartureTime());

                    return RouteStop.builder()
                            .id(UUID.randomUUID().toString())
                            .routeId(newRoute.getId())
                            .stopOrder(point.getStopOrder())
                            .creator(request.getData().getCreator())
                            .createdAt(LocalDateTime.now())
                            .createdBy(request.getData().getCreator())
                            .plannedArrivalTime(arrival)
                            .plannedDepartureTime(departure)
                            .note(point.getNote())
                            .build();
                })
                .collect(Collectors.toList());

        routeStopRepository.saveAll(routeStopList);


        return CreateRouteResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(CreateRouteResponse.CreateRouteResponseData.builder()
                        .id(newRoute.getId())
                        .routeCode(routeCode)
                        .origin(request.getData().getOrigin())
                        .destination(request.getData().getDestination())
                        .plannedStartTime(request.getData().getPlannedStartTime())
                        .plannedEndTime(request.getData().getPlannedEndTime())
                        .status(newRoute.getStatus().name())
                        .stopPoints(request.getData().getStopPoints())
                        .build())
                .build();
    }

    @Override
    public AssignRouteResponse assignRoute(AssignRouteRequest request) {
        if(routeAssignmentRepository.existsByRouteId(request.getData().getRouteId())) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_ROUTE_ASSIGNMENT, request.getData().getRouteId())));
        }

        if(!vehicleRepository.existsById(request.getData().getVehicleId())) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND));
        }

        RouteAssignment routeAssignment = RouteAssignment
                .builder()
                .id(UUID.randomUUID().toString())
                .routeId(request.getData().getRouteId())
                .creator(request.getData().getCreator())
                .vehicleId(request.getData().getVehicleId())
                .assignedAt(OffsetDateTime.now())
                .status(RouteAssignmentStatus.ASSIGNED)
                .build();

        return AssignRouteResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .data(AssignRouteResponse.AssignRouteResponseData.builder()
                        .creator(request.getData().getCreator())
                        .assignedAt(routeAssignment.getAssignedAt().toString())
                        .routeId(routeAssignment.getRouteId())
                        .vehicleId(routeAssignment.getVehicleId())
                        .status(routeAssignment.getStatus().name())
                        .build())
                .build();
    }

    private void validateStopPoints(CreateRouteRequest request) {

        List<CreateRouteRequest.RouteStopPoints> stopPoints = request.getData().getStopPoints();

        Set<Integer> setOfOrders = new HashSet<>();

        for(CreateRouteRequest.RouteStopPoints point : stopPoints) {
            if(point.getStopOrder() == null || Integer.parseInt(point.getStopOrder()) <= 0) {
                throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_STOP_ORDER));
            }

            if(!setOfOrders.add(Integer.valueOf(point.getStopOrder()))) {
                throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_STOP_ORDER));
            }

            if(point.getPlannedArrivalTime() == null || point.getPlannedDepartureTime() == null) {
                throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PLANNED_TIME));
            }

            OffsetDateTime plannedArrivalTime = OffsetDateTime.parse(point.getPlannedArrivalTime());
            OffsetDateTime plannedDepartureTime = OffsetDateTime.parse(point.getPlannedDepartureTime());

            if(!plannedArrivalTime.isBefore(plannedDepartureTime)) {
                throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PLANNED_TIME));
            }
        }

    }
}
