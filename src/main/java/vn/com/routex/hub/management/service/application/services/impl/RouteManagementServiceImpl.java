package vn.com.routex.hub.management.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.services.RouteManagementService;
import vn.com.routex.hub.management.service.application.specification.RouteSpecification;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignment;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignmentRepository;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.management.service.domain.location.LocationRepository;
import vn.com.routex.hub.management.service.domain.route.Route;
import vn.com.routex.hub.management.service.domain.route.RouteRepository;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.domain.seat.RouteSeat;
import vn.com.routex.hub.management.service.domain.seat.RouteSeatRepository;
import vn.com.routex.hub.management.service.domain.seat.SeatStatus;
import vn.com.routex.hub.management.service.domain.stoppoint.RouteStop;
import vn.com.routex.hub.management.service.domain.stoppoint.RouteStopRepository;
import vn.com.routex.hub.management.service.domain.vehicle.Vehicle;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.assignment.AssignRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.location.LocationCodeProjection;
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.CreateRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.seat.RouteSeatView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_EXIST;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class RouteManagementServiceImpl implements RouteManagementService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final RouteAssignmentRepository routeAssignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final LocationRepository locationRepository;
    private final RouteSeatRepository routeSeatRepository;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());


    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Override
    @Transactional
    public CreateRouteResponse createRoute(CreateRouteRequest request) {
        String origin = request.getData().getOrigin().trim();
        String destination = request.getData().getDestination().trim();

        OffsetDateTime plannedStartTime = OffsetDateTime.parse(request.getData().getPlannedStartTime());
        OffsetDateTime plannedEndTime = OffsetDateTime.parse(request.getData().getPlannedEndTime());


        LocationCodeProjection codeResult = locationRepository.selectLocationCode(request.getData().getOrigin(), request.getData().getDestination());

        String originCode = codeResult.getOriginCode();
        String destinationCode = codeResult.getDestinationCode();

        String routeCode = routeRepository.generateRouteCode(originCode, destinationCode);

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
                .pickupBranch(request.getData().getPickupBranch())
                .routeCode(routeCode)
                .origin(origin)
                .creator(request.getData().getCreator())
                .destination(destination)
                .plannedStartTime(plannedStartTime)
                .plannedEndTime(plannedEndTime)
                .status(RouteStatus.PLANNED)
                .createdAt(OffsetDateTime.now())
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
                            .createdAt(OffsetDateTime.now())
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
                        .creator(request.getData().getCreator())
                        .pickupBranch(request.getData().getPickupBranch())
                        .origin(request.getData().getOrigin())
                        .destination(request.getData().getDestination())
                        .plannedStartTime(request.getData().getPlannedStartTime())
                        .plannedEndTime(request.getData().getPlannedEndTime())
                        .status(newRoute.getStatus().name())
                        .stopPoints(request.getData().getStopPoints())
                        .build())
                .build();
    }


    private List<String> generateSeatNos(Vehicle vehicle) {
        int seatCapacity = vehicle.getSeatCapacity();
        boolean hasFloor = vehicle.isHasFloor();

        if(seatCapacity <= 0) return List.of();

        if(!hasFloor) {
            return IntStream.rangeClosed(1, seatCapacity)
                    .mapToObj(i -> String.format("%02d", i))
                    .toList();
        }

        int half = (seatCapacity + 1) / 2;
        List<String> seatNos = new ArrayList<>(seatCapacity);
        for (int i = 1; i <= Math.min(half, seatCapacity); i++) {
            seatNos.add("A" + String.format("%02d", i));
        }
        for (int i = half + 1; i <= seatCapacity; i++) {
            seatNos.add("B" + String.format("%02d", i));
        }

        sLog.info("SeatNos: {}", seatNos);
        return seatNos;
    }

    @Override
    @Transactional
    public AssignRouteResponse assignRoute(AssignRouteRequest request) {
        if(routeAssignmentRepository.existsByRouteId(request.getData().getRouteId())) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_ROUTE_ASSIGNMENT, request.getData().getRouteId())));
        }

        Vehicle vehicle = vehicleRepository.findById(request.getData().getVehicleId())
                .orElseThrow(() -> new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        RouteAssignment routeAssignment = RouteAssignment
                .builder()
                .id(UUID.randomUUID().toString())
                .routeId(request.getData().getRouteId())
                .creator(request.getData().getCreator())
                .vehicleId(vehicle.getId())
                .assignedAt(OffsetDateTime.now())
                .status(RouteAssignmentStatus.ASSIGNED)
                .build();

        Route route = routeRepository.findById(request.getData().getRouteId())
                        .orElseThrow(() -> new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, request.getData().getRouteId()))));


        route.setStatus(RouteStatus.ASSIGNED);
        routeAssignmentRepository.save(routeAssignment);

        if(routeSeatRepository.existsByRouteId(request.getData().getRouteId())) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(ROUTE_SEAT_EXIST, request.getData().getRouteId())));
        }

        List<String> seatNos = generateSeatNos(vehicle);

        sLog.info("Seat Nos: {}", seatNos);

        List<RouteSeat> seats = seatNos.stream()
                .map(seatNo -> RouteSeat.builder()
                        .routeId(request.getData().getRouteId())
                        .seatNo(seatNo)
                        .status(SeatStatus.AVAILABLE)
                        .creator(request.getData().getCreator())
                        .build())
                .collect(Collectors.toList());

        routeSeatRepository.saveAll(seats);

        return AssignRouteResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(AssignRouteResponse.AssignRouteResponseData.builder()
                        .creator(request.getData().getCreator())
                        .assignedAt(routeAssignment.getAssignedAt().toString())
                        .routeId(routeAssignment.getRouteId())
                        .vehicleId(routeAssignment.getVehicleId())
                        .status(routeAssignment.getStatus().name())
                        .build())
                .build();
    }

    @Override
    public SearchRouteResponse searchRoute(SearchRouteRequest request) {

        int pageSize = DateTimeUtils.parseIntOrThrow(request.getData().getPageSize(), "pageSize", request);
        int pageNumber = DateTimeUtils.parseIntOrThrow(request.getData().getPageNumber(), "pageNumber", request);

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 0) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        LocalDate departureDate = DateTimeUtils.parseDateOrThrow(request.getData().getDepartureDate(), "departureDate", request);
        LocalTime fromTime = DateTimeUtils.parseTimeNullable(request.getData().getFromTime(), "fromTime", request); // HH:mm optional
        LocalTime toTime = DateTimeUtils.parseTimeNullable(request.getData().getToTime(), "toTime", request);       // HH:mm optional

        if(fromTime != null && toTime != null && fromTime.isAfter(toTime)) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEARCH_TIME));
        }

        OffsetDateTime startEx = RouteSpecification.dayStart(departureDate, DEFAULT_ZONE);
        OffsetDateTime endEx = RouteSpecification.dayEndExclusive(departureDate, DEFAULT_ZONE);

        if(fromTime != null) startEx = RouteSpecification.atTime(departureDate, fromTime, DEFAULT_ZONE);
        if(toTime != null) endEx = RouteSpecification.atTime(departureDate, toTime, DEFAULT_ZONE);

        Specification<Route> spec = Specification.where(RouteSpecification.originContainsIgnoreCase(request.getData().getOrigin()))
                .and(RouteSpecification.destinationContainsIgnoreCase(request.getData().getDestination()))
                .and(RouteSpecification.plannedStartBetween(startEx, endEx))
                .and(RouteSpecification.assignedStatus(RouteStatus.ASSIGNED));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "plannedStartTime"));

        Page<Route> pageResult = routeRepository.findAll(spec, pageable);

        List<String> routeIds = pageResult.getContent().stream().map(Route::getId).toList();


        Map<String, RouteAssignment> assignments;
        Map<String, Long> seatAvailable;
        Map<String, Vehicle> vehicleById;
        if(!routeIds.isEmpty()) {
            seatAvailable = routeSeatRepository
                    .countByRouteIdAndStatus(routeIds, SeatStatus.AVAILABLE.name())
                    .stream()
                    .collect(Collectors.toMap(RouteSeatView::getRouteId, RouteSeatView::getAvailableSeat));

            List<RouteAssignment> assigns = routeAssignmentRepository.findActiveByRouteIdsNative(routeIds, RouteAssignmentStatus.ASSIGNED.name());

            assignments = assigns.stream()
                    .collect(Collectors.groupingBy(RouteAssignment::getRouteId))
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().stream()
                                    .max(Comparator.comparing(RouteAssignment::getAssignedAt,
                                            Comparator.nullsLast(Comparator.naturalOrder())))
                                    .orElse(null)
                    ));

            List<String> vehicleIds = assignments.values().stream()
                    .filter(Objects::nonNull)
                    .map(RouteAssignment::getVehicleId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();

            if (!vehicleIds.isEmpty()) {
                vehicleById = vehicleRepository.findByIdIn(vehicleIds).stream()
                        .collect(Collectors.toMap(Vehicle::getId, v -> v));
            } else {
                vehicleById = Map.of();
            }

        } else {
            vehicleById = Map.of();
            assignments = Map.of();
            seatAvailable = Map.of();
        }

        Map<String, List<SearchRouteResponse.SearchStopPoints>> stopsByRouteId;
        if (!routeIds.isEmpty()) {
            var stops = routeStopRepository.findByRouteIdIn(routeIds);
            stops.sort(Comparator.comparingInt(s -> Integer.parseInt(s.getStopOrder())));
            stopsByRouteId = stops.stream().collect(Collectors.groupingBy(
                    RouteStop::getRouteId,
                    Collectors.mapping(this::toStopPoints, Collectors.toList())
            ));
        } else {
            stopsByRouteId = Map.of();
        }

        List<SearchRouteResponse.SearchRouteResponseData> items = pageResult.getContent().stream()
                .map(r ->
                {
                    RouteAssignment ra = assignments.get(r.getId());
                    Vehicle v = vehicleById.get(ra.getVehicleId());
                    return SearchRouteResponse.SearchRouteResponseData.builder()
                            .id(r.getId())
                            .routeCode(r.getRouteCode())
                            .pickupBranch(r.getPickupBranch())
                            .origin(r.getOrigin())
                            .destination(r.getDestination())
                            .availableSeats(seatAvailable.getOrDefault(r.getId(), 0L))
                            .plannedStartTime(r.getPlannedStartTime())
                            .plannedEndTime(r.getPlannedEndTime())
                            // .price
                            //
                            .vehiclePlate(v.getVehiclePlate())
                            .hasFloor(v.isHasFloor())
                            .stopPoints(stopsByRouteId.getOrDefault(r.getId(), List.of()))
                            .build();
                })
                .collect(Collectors.toList());

        return SearchRouteResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(items)
                .build();
    }

    private SearchRouteResponse.SearchStopPoints toStopPoints(RouteStop s) {
        return SearchRouteResponse.SearchStopPoints.builder()
                .id(s.getId())
                .stopOrder(s.getStopOrder())
                .plannedArrivalTime(s.getStopOrder())
                .routeId(s.getRouteId())
                .plannedArrivalTime(s.getPlannedArrivalTime().toString())
                .plannedDepartureTime(s.getPlannedDepartureTime().toString())
                .note(s.getNote())
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
