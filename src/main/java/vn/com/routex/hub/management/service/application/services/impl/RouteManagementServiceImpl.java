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
import vn.com.routex.hub.management.service.application.command.route.FetchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesResult;
import vn.com.routex.hub.management.service.application.command.route.RoutePointCommand;
import vn.com.routex.hub.management.service.application.command.route.RoutePointResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteItemResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.UpdateRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.UpdateRouteResult;
import vn.com.routex.hub.management.service.application.services.RouteManagementService;
import vn.com.routex.hub.management.service.application.specification.RouteSpecification;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.domain.route.model.ProvincesCodePair;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.domain.route.port.RoutePointRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteProvincesLookupPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteQueryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSaleEventPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteFetchView;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteSearchView;
import vn.com.routex.hub.management.service.domain.operationpoint.port.OperationPointRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_POINT_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.OPERATION_POINT_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class RouteManagementServiceImpl implements RouteManagementService {

    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RoutePointRepositoryPort routePointRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final RouteVehicleRepositoryPort routeVehicleRepositoryPort;
    private final RouteProvincesLookupPort routeProvincesLookupPort;
    private final RouteSeatAvailabilityPort routeSeatAvailabilityPort;
    private final RouteQueryPort routeQueryPort;
    private final RouteSaleEventPort routeSaleEventPort;
    private final OperationPointRepositoryPort operationPointRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private static int parseIntOrDefault(
            String v,
            int defaultValue,
            String field,
            String requestId,
            String requestDateTime,
            String channel
    ) {
        if (v == null || v.isBlank()) return defaultValue;
        return DateTimeUtils.parseIntOrThrow(v, field, requestId, requestDateTime, channel);
    }

    @Override
    @Transactional
    public CreateRouteResult createRoute(CreateRouteCommand command) {
        String origin = command.origin().trim();
        String destination = command.destination().trim();

        OffsetDateTime plannedStartTime = OffsetDateTime.parse(command.plannedStartTime());
        OffsetDateTime plannedEndTime = OffsetDateTime.parse(command.plannedEndTime());

        ProvincesCodePair codeResult = routeProvincesLookupPort.getCodes(command.origin(), command.destination());

        String originCode = codeResult.originCode();
        String destinationCode = codeResult.destinationCode();

        String routeCode = routeAggregateRepositoryPort.generateRouteCode(originCode, destinationCode);

        if(!plannedStartTime.isBefore(plannedEndTime)) {
            throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_START_TIME));
        }

        List<RoutePointCommand> routePoints =
                Optional.ofNullable(command.routePoints())
                        .orElseGet(List::of);

        // Validate stop points
        validateRoutePoints(command);

        OffsetDateTime now = OffsetDateTime.now();
        String routeId = UUID.randomUUID().toString();
        List<RouteStopPlan> routeStopPlans = routePoints.stream()
                .map(point -> {
                    OffsetDateTime arrival = OffsetDateTime.parse(point.plannedArrivalTime());
                    OffsetDateTime departure = OffsetDateTime.parse(point.plannedDepartureTime());

                    return RouteStopPlan.builder()
                            .id(UUID.randomUUID().toString())
                            .routeId(routeId)
                            .stopOrder(Integer.parseInt(point.operationOrder()))
                            .creator(command.creator())
                            .createdAt(now)
                            .createdBy(command.creator())
                            .plannedArrivalTime(arrival)
                            .plannedDepartureTime(departure)
                            .note(point.note())
                            .operationPointId(point.operationPointId())
                            .stopName(point.stopName())
                            .stopAddress(point.stopAddress())
                            .stopCity(point.stopCity())
                            .stopLatitude(point.stopLatitude())
                            .stopLongitude(point.stopLongitude())
                            .build();
                })
                .collect(Collectors.toList());

        RouteAggregate newRoute = RouteAggregate.plan(
                routeId,
                routeCode,
                command.creator(),
                command.pickupBranch(),
                origin,
                destination,
                plannedStartTime,
                plannedEndTime,
                now,
                routeStopPlans
        );

        routeAggregateRepositoryPort.save(newRoute);
        routePointRepositoryPort.saveAll(routeStopPlans);


        return CreateRouteResult.builder()
                .id(newRoute.getId())
                .routeCode(routeCode)
                .creator(command.creator())
                .pickupBranch(command.pickupBranch())
                .origin(command.origin())
                .destination(command.destination())
                .plannedStartTime(command.plannedStartTime())
                .plannedEndTime(command.plannedEndTime())
                .status(RouteStatus.PLANNED.name())
                .routePoints(command.routePoints() != null ?
                        command.routePoints() : null)
                .build();
    }

    @Override
    @Transactional
    public AssignRouteResult assignRoute(AssignRouteCommand command) {
        if(routeAssignmentRepositoryPort.existsActiveByRouteId(command.routeId())) {
            throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_ROUTE_ASSIGNMENT, command.routeId())));
        }

        VehicleSnapshot vehicle = routeVehicleRepositoryPort.findById(command.vehicleId())
                .orElseThrow(() -> new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        RouteAggregate route = routeAggregateRepositoryPort.findById(command.routeId())
                        .orElseThrow(() -> new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))));

        OffsetDateTime assignedAt = OffsetDateTime.now();
        RouteAssignmentRecord routeAssignment = RouteAssignmentRecord.assign(
                UUID.randomUUID().toString(),
                command.routeId(),
                command.creator(),
                vehicle.getId(),
                command.driverId(),
                assignedAt
        );

        route.assign();
        routeAggregateRepositoryPort.save(route);
        routeAssignmentRepositoryPort.save(routeAssignment);

        RouteSellableEvent event = RouteSellableEvent
                .builder()
                .routeId(routeAssignment.getRouteId())
                .vehicleId(routeAssignment.getVehicleId())
                .assignedBy(command.creator())
                .assignedAt(routeAssignment.getAssignedAt())
                .routeStatus(RouteStatus.ASSIGNED.name())
                .seatCount(vehicle.getSeatCapacity())
                .hasFloor(vehicle.isHasFloor())
                .creator(command.creator())
                .build();

        routeSaleEventPort.publishRouteReadyForSale(
                command.requestId(),
                command.requestDateTime(),
                command.channel(),
                routeAssignment.getRouteId(),
                event
        );
        return AssignRouteResult.builder()
                .creator(command.creator())
                .assignedAt(routeAssignment.getAssignedAt().toString())
                .routeId(routeAssignment.getRouteId())
                .vehicleId(routeAssignment.getVehicleId())
                .driverId(routeAssignment.getDriverId())
                .status(routeAssignment.getStatus().name())
                .build();
    }

    @Override
    @Transactional
    public UpdateRouteResult updateRoute(UpdateRouteCommand command) {
        RouteAggregate route = routeAggregateRepositoryPort.findById(command.routeId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))));

        if(command.routePoints() != null) {
            command.routePoints().forEach(point -> {
                RouteStopPlan routeStopPlan = routePointRepositoryPort.findByRouteIdAndStopOrder(command.routeId(), point.operationOrder())
                        .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROUTE_POINT_NOT_FOUND)));

                Optional.ofNullable(point.plannedArrivalTime())
                        .ifPresent(routeStopPlan::setPlannedArrivalTime);

                Optional.ofNullable(point.plannedDepartureTime())
                        .ifPresent(routeStopPlan::setPlannedDepartureTime);

                Optional.ofNullable(point.note())
                        .ifPresent(routeStopPlan::setNote);

                routePointRepositoryPort.save(routeStopPlan);
            });
        }
        Optional.ofNullable(command.pickupBranch())
                .ifPresent(route::setPickupBranch);

        Optional.ofNullable(command.origin())
                .ifPresent(route::setOrigin);

        Optional.ofNullable(command.destination())
                .ifPresent(route::setDestination);

        Optional.ofNullable(command.plannedStartTime())
                .ifPresent(route::setPlannedStartTime);

        Optional.ofNullable(command.plannedEndTime())
                .ifPresent(route::setPlannedEndTime);

        Optional.ofNullable(command.actualStartTime())
                .ifPresent(route::setActualStartTime);

        Optional.ofNullable(command.actualEndTime())
                .ifPresent(route::setActualEndTime);

        routeAggregateRepositoryPort.save(route);

        List<UpdateRouteResult.UpdateRoutePointResult> routePointResults = command.routePoints() != null ?
                command.routePoints().stream()
                .map(point -> UpdateRouteResult.UpdateRoutePointResult.builder()
                        .id(point.id())
                        .operationOrder(point.id())
                        .plannedArrivalTime(point.plannedArrivalTime())
                        .plannedDepartureTime(point.plannedDepartureTime())
                        .note(point.note())
                        .build())
                .toList() : null;

        return UpdateRouteResult.builder()
                .routeId(command.routeId())
                .creator(command.creator())
                .pickupBranch(command.pickupBranch())
                .origin(command.origin())
                .destination(command.destination())
                .plannedStartTime(command.plannedStartTime())
                .plannedEndTime(command.plannedEndTime())
                .actualStartTime(command.actualStartTime())
                .actualEndTime(command.actualEndTime())
                .status(command.status())
                .routePoints(routePointResults)
                .build();
    }

    @Override
    public SearchRouteResult searchRoute(SearchRouteQuery query) {
        int pageSize = parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.requestId(), query.requestDateTime(), query.channel());
        int pageNumber = parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.requestId(), query.requestDateTime(), query.channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.requestId(), query.requestDateTime(), query.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.requestId(), query.requestDateTime(), query.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        LocalDate departureDate = DateTimeUtils.parseDateOrThrow(query.departureDate(), "departureDate",
                query.requestId(), query.requestDateTime(), query.channel());
        LocalTime fromTime = DateTimeUtils.parseTimeNullable(query.fromTime(), "fromTime",
                query.requestId(), query.requestDateTime(), query.channel());
        LocalTime toTime = DateTimeUtils.parseTimeNullable(query.toTime(), "toTime",
                query.requestId(), query.requestDateTime(), query.channel());

        if(fromTime != null && toTime != null && fromTime.isAfter(toTime)) {
            throw new BusinessException(query.requestId(), query.requestDateTime(), query.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEARCH_TIME));
        }

        OffsetDateTime startEx = RouteSpecification.dayStart(departureDate, DEFAULT_ZONE);
        OffsetDateTime endEx = RouteSpecification.dayEndExclusive(departureDate, DEFAULT_ZONE);

        if(fromTime != null) startEx = RouteSpecification.atTime(departureDate, fromTime, DEFAULT_ZONE);
        if(toTime != null) endEx = RouteSpecification.atTime(departureDate, toTime, DEFAULT_ZONE);

        List<RouteSearchView> searchedRoutes = routeQueryPort.searchAssignedRoutes(
                query.origin(),
                query.destination(),
                startEx,
                endEx,
                pageNumber - 1, // external is 1-based; Spring Data is 0-based
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
        stopsByRouteId = routeIds.isEmpty() ? Map.of() : routePointRepositoryPort.findByRouteIds(routeIds);

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
                            .routePoints(stopsByRouteId.getOrDefault(r.getId(), List.of()).stream()
                                    .map(this::toRoutePoint)
                                    .toList())
                            .build();
                })
                .collect(Collectors.toList());

        return SearchRouteResult.builder()
                .data(items)
                .build();
    }

    @Override
    public FetchRoutesResult fetchRoutes(FetchRoutesQuery query) {
        int pageSize = parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.requestId(), query.requestDateTime(), query.channel());
        int pageNumber = parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.requestId(), query.requestDateTime(), query.channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.requestId(), query.requestDateTime(), query.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.requestId(), query.requestDateTime(), query.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        // external is 1-based; Spring Data is 0-based
        PagedResult<RouteFetchView> page = routeQueryPort.fetchRoutes(pageNumber - 1, pageSize);
        List<RouteFetchView> routes = page.getItems();

        List<String> routeIds = routes.stream()
                .map(RouteFetchView::getId)
                .toList();

        Map<String, RouteAssignmentRecord> assignments;
        Map<String, Long> seatAvailable;
        Map<String, VehicleSnapshot> vehicleById;
        if (!routeIds.isEmpty()) {
            seatAvailable = routeSeatAvailabilityPort.countAvailableSeats(routeIds);
            assignments = routeAssignmentRepositoryPort.findLatestActiveByRouteIds(routeIds);

            List<String> vehicleIds = assignments.values().stream()
                    .map(RouteAssignmentRecord::getVehicleId)
                    .distinct()
                    .toList();

            vehicleById = vehicleIds.isEmpty() ? Map.of() : routeVehicleRepositoryPort.findByIds(vehicleIds);
        } else {
            vehicleById = Map.of();
            assignments = Map.of();
            seatAvailable = Map.of();
        }

        Map<String, List<RouteStopPlan>> stopsByRouteId =
                routeIds.isEmpty() ? Map.of() : routePointRepositoryPort.findByRouteIds(routeIds);

        List<FetchRouteResult> items = routes.stream()
                .map(r -> {
                    RouteAssignmentRecord ra = assignments.get(r.getId());
                    VehicleSnapshot v = ra == null ? null : vehicleById.get(ra.getVehicleId());
                    return FetchRouteResult.builder()
                            .id(r.getId())
                            .creator(r.getCreator())
                            .pickupBranch(r.getPickupBranch())
                            .routeCode(r.getRouteCode())
                            .origin(r.getOrigin())
                            .destination(r.getDestination())
                            .plannedStartTime(r.getPlannedStartTime())
                            .plannedEndTime(r.getPlannedEndTime())
                            .actualStartTime(r.getActualStartTime())
                            .actualEndTime(r.getActualEndTime())
                            .status(r.getStatus())
                            .availableSeats(seatAvailable.getOrDefault(r.getId(), 0L))
                            .vehicleId(ra == null ? null : ra.getVehicleId())
                            .vehiclePlate(v == null ? null : v.getVehiclePlate())
                            .hasFloor(v != null && v.isHasFloor())
                            .assignedAt(ra == null ? null : ra.getAssignedAt())
                            .routePoints(stopsByRouteId.getOrDefault(r.getId(), List.of()).stream()
                                    .map(this::toRoutePoint)
                                    .toList())
                            .build();
                })
                .collect(Collectors.toList());

        return FetchRoutesResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public DeleteRouteResult deleteRoute(DeleteRouteCommand command) {
        RouteAggregate route = routeAggregateRepositoryPort.findById(command.routeId())
                .orElseThrow(() -> new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))));

        OffsetDateTime now = OffsetDateTime.now();
        route.cancel(command.creator(), now);
        routeAggregateRepositoryPort.save(route);

        routeAssignmentRepositoryPort
                .findActiveByRouteId(route.getId())
                .ifPresent(routeAssignment -> {
                    routeAssignment.cancel(command.creator(), now);
                    routeAssignmentRepositoryPort.save(routeAssignment);
                });

        return DeleteRouteResult.builder()
                .creator(command.creator())
                .routeId(route.getId())
                .routeCode(route.getRouteCode())
                .status(route.getStatus().name())
                .updatedAt(route.getUpdatedAt())
                .build();
    }

    private RoutePointResult toRoutePoint(RouteStopPlan s) {
        return RoutePointResult.builder()
                .id(s.getId())
                .operationOrder(String.valueOf(s.getStopOrder()))
                .routeId(s.getRouteId())
                .plannedArrivalTime(s.getPlannedArrivalTime() == null ? null : s.getPlannedArrivalTime().toString())
                .plannedDepartureTime(s.getPlannedDepartureTime() == null ? null : s.getPlannedDepartureTime().toString())
                .note(s.getNote())
                .operationPointId(s.getOperationPointId())
                .stopName(s.getStopName())
                .stopAddress(s.getStopAddress())
                .stopCity(s.getStopCity())
                .stopLatitude(s.getStopLatitude())
                .stopLongitude(s.getStopLongitude())
                .build();
    }

    private void validateRoutePoints(CreateRouteCommand command) {
        List<RoutePointCommand> routePoints = command.routePoints();
        if (routePoints == null || routePoints.isEmpty()) return;

        Set<Integer> setOfOrders = new HashSet<>();

        for(RoutePointCommand point : routePoints) {
            if(point.operationOrder() == null || Integer.parseInt(point.operationOrder()) <= 0) {
                throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_STOP_ORDER));
            }

            if(!setOfOrders.add(Integer.valueOf(point.operationOrder()))) {
                throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_STOP_ORDER));
            }

            if(point.plannedArrivalTime() == null || point.plannedDepartureTime() == null) {
                throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PLANNED_TIME));
            }

            OffsetDateTime plannedArrivalTime = OffsetDateTime.parse(point.plannedArrivalTime());
            OffsetDateTime plannedDepartureTime = OffsetDateTime.parse(point.plannedDepartureTime());

            if(!plannedArrivalTime.isBefore(plannedDepartureTime)) {
                throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PLANNED_TIME));
            }

            boolean hasOpId = point.operationPointId() != null && !point.operationPointId().isBlank();
            boolean hasCustomName = point.stopName() != null && !point.stopName().isBlank();
            if (hasOpId == hasCustomName) {
                throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "Either operationPointId or stopName is required (but not both)"));
            }

            if (hasOpId) {
                operationPointRepositoryPort.findById(point.operationPointId().trim())
                        .orElseThrow(() -> new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND,
                                        String.format(OPERATION_POINT_NOT_FOUND, point.operationPointId().trim()))));
            } else {
                // custom stop: basic coordinate sanity
                if (point.stopLatitude() != null ^ point.stopLongitude() != null) {
                    throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                            ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "stopLatitude and stopLongitude must be provided together"));
                }
            }
        }

    }
}
