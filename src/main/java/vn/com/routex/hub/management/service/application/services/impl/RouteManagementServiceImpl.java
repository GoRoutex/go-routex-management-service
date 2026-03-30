package vn.com.routex.hub.management.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
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
import vn.com.routex.hub.management.service.application.specification.RouteSpecification;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.domain.route.model.ProvincesCodePair;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteProvincesLookupPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteQueryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSaleEventPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.route.port.OperationPointRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteSearchView;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ROUTE_ASSIGNMENT;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PLANNED_TIME;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_SEARCH_TIME;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_START_TIME;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_STOP_ORDER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class RouteManagementServiceImpl implements RouteManagementService {

    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final OperationPointRepositoryPort operationPointRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final RouteVehicleRepositoryPort routeVehicleRepositoryPort;
    private final RouteProvincesLookupPort routeProvincesLookupPort;
    private final RouteSeatAvailabilityPort routeSeatAvailabilityPort;
    private final RouteQueryPort routeQueryPort;
    private final RouteSaleEventPort routeSaleEventPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());


    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Override
    @Transactional
    public CreateRouteResult createRoute(CreateRouteCommand command) {
        String origin = command.getOrigin().trim();
        String destination = command.getDestination().trim();

        OffsetDateTime plannedStartTime = OffsetDateTime.parse(command.getPlannedStartTime());
        OffsetDateTime plannedEndTime = OffsetDateTime.parse(command.getPlannedEndTime());

        ProvincesCodePair codeResult = routeProvincesLookupPort.getCodes(command.getOrigin(), command.getDestination());

        String originCode = codeResult.originCode();
        String destinationCode = codeResult.destinationCode();

        String routeCode = routeAggregateRepositoryPort.generateRouteCode(originCode, destinationCode);

        if(!plannedStartTime.isBefore(plannedEndTime)) {
            throw new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_START_TIME));
        }

        List<OperationPointCommand> operationPoints =
                Optional.ofNullable(command.getOperationPoints())
                        .orElseGet(List::of);

        // Validate stop points
        validateOperationPoints(command);

        OffsetDateTime now = OffsetDateTime.now();
        String routeId = UUID.randomUUID().toString();
        List<RouteStopPlan> routeStopPlans = operationPoints.stream()
                .map(point -> {
                    OffsetDateTime arrival = OffsetDateTime.parse(point.getPlannedArrivalTime());
                    OffsetDateTime departure = OffsetDateTime.parse(point.getPlannedDepartureTime());

                    return RouteStopPlan.builder()
                            .id(UUID.randomUUID().toString())
                            .routeId(routeId)
                            .stopOrder(Integer.parseInt(point.getOperationOrder()))
                            .creator(command.getCreator())
                            .createdAt(now)
                            .createdBy(command.getCreator())
                            .plannedArrivalTime(arrival)
                            .plannedDepartureTime(departure)
                            .note(point.getNote())
                            .build();
                })
                .collect(Collectors.toList());

        RouteAggregate newRoute = RouteAggregate.plan(
                routeId,
                routeCode,
                command.getCreator(),
                command.getPickupBranch(),
                origin,
                destination,
                plannedStartTime,
                plannedEndTime,
                now,
                routeStopPlans
        );

        routeAggregateRepositoryPort.save(newRoute);
        operationPointRepositoryPort.saveAll(routeStopPlans);


        return CreateRouteResult.builder()
                .id(newRoute.getId())
                .routeCode(routeCode)
                .creator(command.getCreator())
                .pickupBranch(command.getPickupBranch())
                .origin(command.getOrigin())
                .destination(command.getDestination())
                .plannedStartTime(command.getPlannedStartTime())
                .plannedEndTime(command.getPlannedEndTime())
                .status(RouteStatus.PLANNED.name())
                .operationPoints(new ArrayList<>(command.getOperationPoints()))
                .build();
    }

    @Override
    @Transactional
    public AssignRouteResult assignRoute(AssignRouteCommand command) {
        if(routeAssignmentRepositoryPort.existsActiveByRouteId(command.getRouteId())) {
            throw new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_ROUTE_ASSIGNMENT, command.getRouteId())));
        }

        VehicleSnapshot vehicle = routeVehicleRepositoryPort.findById(command.getVehicleId())
                .orElseThrow(() -> new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        RouteAggregate route = routeAggregateRepositoryPort.findById(command.getRouteId())
                        .orElseThrow(() -> new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.getRouteId()))));

        OffsetDateTime assignedAt = OffsetDateTime.now();
        RouteAssignmentRecord routeAssignment = RouteAssignmentRecord.assign(
                UUID.randomUUID().toString(),
                command.getRouteId(),
                command.getCreator(),
                vehicle.getId(),
                command.getDriverId(),
                assignedAt
        );

        route.assign();
        routeAggregateRepositoryPort.save(route);
        routeAssignmentRepositoryPort.save(routeAssignment);

        RouteSellableEvent event = RouteSellableEvent
                .builder()
                .routeId(routeAssignment.getRouteId())
                .vehicleId(routeAssignment.getVehicleId())
                .assignedBy(command.getCreator())
                .assignedAt(routeAssignment.getAssignedAt())
                .routeStatus(RouteStatus.ASSIGNED.name())
                .seatCount(vehicle.getSeatCapacity())
                .hasFloor(vehicle.isHasFloor())
                .creator(command.getCreator())
                .build();

        routeSaleEventPort.publishRouteReadyForSale(
                command.getRequestId(),
                command.getRequestDateTime(),
                command.getChannel(),
                routeAssignment.getRouteId(),
                event
        );
        return AssignRouteResult.builder()
                .creator(command.getCreator())
                .assignedAt(routeAssignment.getAssignedAt().toString())
                .routeId(routeAssignment.getRouteId())
                .vehicleId(routeAssignment.getVehicleId())
                .driverId(routeAssignment.getDriverId())
                .status(routeAssignment.getStatus().name())
                .build();
    }

    @Override
    public SearchRouteResult searchRoute(SearchRouteQuery query) {

        int pageSize = DateTimeUtils.parseIntOrThrow(query.getPageSize(), "pageSize",
                query.getRequestId(), query.getRequestDateTime(), query.getChannel());
        int pageNumber = DateTimeUtils.parseIntOrThrow(query.getPageNumber(), "pageNumber",
                query.getRequestId(), query.getRequestDateTime(), query.getChannel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.getRequestId(), query.getRequestDateTime(), query.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 0) {
            throw new BusinessException(query.getRequestId(), query.getRequestDateTime(), query.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        LocalDate departureDate = DateTimeUtils.parseDateOrThrow(query.getDepartureDate(), "departureDate",
                query.getRequestId(), query.getRequestDateTime(), query.getChannel());
        LocalTime fromTime = DateTimeUtils.parseTimeNullable(query.getFromTime(), "fromTime",
                query.getRequestId(), query.getRequestDateTime(), query.getChannel());
        LocalTime toTime = DateTimeUtils.parseTimeNullable(query.getToTime(), "toTime",
                query.getRequestId(), query.getRequestDateTime(), query.getChannel());

        if(fromTime != null && toTime != null && fromTime.isAfter(toTime)) {
            throw new BusinessException(query.getRequestId(), query.getRequestDateTime(), query.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEARCH_TIME));
        }

        OffsetDateTime startEx = RouteSpecification.dayStart(departureDate, DEFAULT_ZONE);
        OffsetDateTime endEx = RouteSpecification.dayEndExclusive(departureDate, DEFAULT_ZONE);

        if(fromTime != null) startEx = RouteSpecification.atTime(departureDate, fromTime, DEFAULT_ZONE);
        if(toTime != null) endEx = RouteSpecification.atTime(departureDate, toTime, DEFAULT_ZONE);

        List<RouteSearchView> searchedRoutes = routeQueryPort.searchAssignedRoutes(
                query.getOrigin(),
                query.getDestination(),
                startEx,
                endEx,
                pageNumber,
                pageSize
        );

        List<String> routeIds = searchedRoutes.stream()
                .map(RouteSearchView::getId)
                .toList();

        Map<String, RouteAssignmentRecord> assignments;
        Map<String, Long> seatAvailable;
        Map<String, VehicleSnapshot> vehicleById;
        if(!routeIds.isEmpty()) {
            seatAvailable = routeSeatAvailabilityPort.countAvailableSeats(routeIds);
            assignments = routeAssignmentRepositoryPort.findLatestActiveByRouteIds(routeIds);

            List<String> vehicleIds = assignments.values().stream()
                    .map(RouteAssignmentRecord::getVehicleId)
                    .distinct()
                    .toList();

            if (!vehicleIds.isEmpty()) {
                vehicleById = routeVehicleRepositoryPort.findByIds(vehicleIds);
            } else {
                vehicleById = Map.of();
            }

        } else {
            vehicleById = Map.of();
            assignments = Map.of();
            seatAvailable = Map.of();
        }

        Map<String, List<RouteStopPlan>> stopsByRouteId;
        stopsByRouteId = routeIds.isEmpty() ? Map.of() : operationPointRepositoryPort.findByRouteIds(routeIds);

        List<SearchRouteItemResult> items = searchedRoutes.stream()
                .map(r ->
                {
                    RouteAssignmentRecord ra = assignments.get(r.getId());
                    VehicleSnapshot v = ra == null ? null : vehicleById.get(ra.getVehicleId());
                    return SearchRouteItemResult.builder()
                            .id(r.getId())
                            .routeCode(r.getRouteCode())
                            .pickupBranch(r.getPickupBranch())
                            .origin(r.getOrigin())
                            .destination(r.getDestination())
                            .availableSeats(seatAvailable.getOrDefault(r.getId(), 0L))
                            .plannedStartTime(r.getPlannedStartTime())
                            .plannedEndTime(r.getPlannedEndTime())
                            .vehiclePlate(v == null ? null : v.getVehiclePlate())
                            .hasFloor(v != null && v.isHasFloor())
                            .operationPoints(stopsByRouteId.getOrDefault(r.getId(), List.of()).stream()
                                    .map(this::toOperationPoint)
                                    .toList())
                            .build();
                })
                .collect(Collectors.toList());

        return SearchRouteResult.builder()
                .data(items)
                .build();
    }

    @Override
    public FetchRouteResult fetchRoute(FetchRouteQuery query) {
        RouteAggregate route = routeAggregateRepositoryPort.findById(query.getRouteId())
                .orElseThrow(() -> new BusinessException(query.getRequestId(), query.getRequestDateTime(), query.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, query.getRouteId()))));

        RouteAssignmentRecord assignment = routeAssignmentRepositoryPort
                .findActiveByRouteId(route.getId())
                .orElse(null);

        VehicleSnapshot vehicle = assignment == null ? null : routeVehicleRepositoryPort.findById(assignment.getVehicleId()).orElse(null);

        Long availableSeats = routeSeatAvailabilityPort.countAvailableSeats(List.of(route.getId()))
                .getOrDefault(route.getId(), 0L);

        List<OperationPointResult> operationPoints = operationPointRepositoryPort.findByRouteId(route.getId()).stream()
                .map(this::toOperationPoint)
                .toList();

        return FetchRouteResult.builder()
                .id(route.getId())
                .creator(route.getCreator())
                .pickupBranch(route.getPickupBranch())
                .routeCode(route.getRouteCode())
                .origin(route.getOrigin())
                .destination(route.getDestination())
                .plannedStartTime(route.getPlannedStartTime())
                .plannedEndTime(route.getPlannedEndTime())
                .actualStartTime(route.getActualStartTime())
                .actualEndTime(route.getActualEndTime())
                .status(route.getStatus().name())
                .availableSeats(availableSeats)
                .vehicleId(assignment == null ? null : assignment.getVehicleId())
                .vehiclePlate(vehicle == null ? null : vehicle.getVehiclePlate())
                .hasFloor(vehicle == null ? null : vehicle.isHasFloor())
                .assignedAt(assignment == null ? null : assignment.getAssignedAt())
                .operationPoints(operationPoints)
                .build();
    }

    @Override
    @Transactional
    public DeleteRouteResult deleteRoute(DeleteRouteCommand command) {
        RouteAggregate route = routeAggregateRepositoryPort.findById(command.getRouteId())
                .orElseThrow(() -> new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.getRouteId()))));

        OffsetDateTime now = OffsetDateTime.now();
        route.cancel(command.getCreator(), now);
        routeAggregateRepositoryPort.save(route);

        routeAssignmentRepositoryPort
                .findActiveByRouteId(route.getId())
                .ifPresent(routeAssignment -> {
                    routeAssignment.cancel(command.getCreator(), now);
                    routeAssignmentRepositoryPort.save(routeAssignment);
                });

        return DeleteRouteResult.builder()
                .creator(command.getCreator())
                .routeId(route.getId())
                .routeCode(route.getRouteCode())
                .status(route.getStatus().name())
                .updatedAt(route.getUpdatedAt())
                .build();
    }

    private OperationPointResult toOperationPoint(RouteStopPlan s) {
        return OperationPointResult.builder()
                .id(s.getId())
                .operationOrder(String.valueOf(s.getStopOrder()))
                .routeId(s.getRouteId())
                .plannedArrivalTime(s.getPlannedArrivalTime().toString())
                .plannedDepartureTime(s.getPlannedDepartureTime().toString())
                .note(s.getNote())
                .build();
    }

    private void validateOperationPoints(CreateRouteCommand command) {

        List<OperationPointCommand> operationPoints = command.getOperationPoints();

        Set<Integer> setOfOrders = new HashSet<>();

        for(OperationPointCommand point : operationPoints) {
            if(point.getOperationOrder() == null || Integer.parseInt(point.getOperationOrder()) <= 0) {
                throw new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_STOP_ORDER));
            }

            if(!setOfOrders.add(Integer.valueOf(point.getOperationOrder()))) {
                throw new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_STOP_ORDER));
            }

            if(point.getPlannedArrivalTime() == null || point.getPlannedDepartureTime() == null) {
                throw new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PLANNED_TIME));
            }

            OffsetDateTime plannedArrivalTime = OffsetDateTime.parse(point.getPlannedArrivalTime());
            OffsetDateTime plannedDepartureTime = OffsetDateTime.parse(point.getPlannedDepartureTime());

            if(!plannedArrivalTime.isBefore(plannedDepartureTime)) {
                throw new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PLANNED_TIME));
            }
        }

    }
}
