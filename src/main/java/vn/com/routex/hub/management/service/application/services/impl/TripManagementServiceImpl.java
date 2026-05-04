package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.common.PageContext;
import vn.com.routex.hub.management.service.application.command.common.PageInfo;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;
import vn.com.routex.hub.management.service.application.command.route.FetchTripQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchTripResult;
import vn.com.routex.hub.management.service.application.command.route.FetchTripsQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchTripsResult;
import vn.com.routex.hub.management.service.application.command.route.RoutePointResult;
import vn.com.routex.hub.management.service.application.command.route.SearchTripItemResult;
import vn.com.routex.hub.management.service.application.command.route.SearchTripQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchTripResult;
import vn.com.routex.hub.management.service.application.command.route.SearchWindow;
import vn.com.routex.hub.management.service.application.services.TripManagementService;
import vn.com.routex.hub.management.service.application.specification.TripSpecification;
import vn.com.routex.hub.management.service.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.hub.management.service.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RoutePointRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.TripQueryPort;
import vn.com.routex.hub.management.service.domain.trip.model.TripAggregate;
import vn.com.routex.hub.management.service.domain.trip.port.TripAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripFetchView;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripSearchView;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_ZONE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.TRIP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TripManagementServiceImpl implements TripManagementService {

    private final RoutePointRepositoryPort routePointRepositoryPort;
    private final TripAssignmentRepositoryPort tripAssignmentRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RouteVehicleRepositoryPort routeVehicleRepositoryPort;
    private final RouteSeatAvailabilityPort routeSeatAvailabilityPort;
    private final TripQueryPort tripQueryPort;
    private final MerchantRepositoryPort merchantRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final TripAggregateRepositoryPort tripAggregateRepositoryPort;

    @Override
    public SearchTripResult searchTrip(SearchTripQuery query) {
        PageInfo pageInfo = validatePageContext(query.context(), query.pageContext());
        SearchWindow searchWindow = resolveSearchWindow(query);

        List<TripSearchView> searchedRoutes = tripQueryPort.searchAssignedTrips(
                null,
                query.originName(),
                query.destinationName(),
                pageInfo.pageNumber() - 1, // external is 1-based; Spring Data is 0-based
                pageInfo.pageSize()
        );

        TripEnrichment enrichment = enrichRoutes(
                searchedRoutes.stream().map(TripSearchView::getId).toList(),
                null
        );

        Map<String, String> merchantNames = searchedRoutes.stream()
                .map(TripSearchView::getMerchantId)
                .distinct()
                .collect(Collectors.toMap(
                        merchantId -> merchantId,
                        this::findMerchantName,
                        (left, right) -> left
                ));

        List<SearchTripItemResult> items = searchedRoutes.stream()
                .map(route -> toSearchRouteItem(route, enrichment, merchantNames))
                .collect(Collectors.toList());

        return SearchTripResult.builder()
                .data(items)
                .build();
    }

    @Override
    public FetchTripResult fetchTripDetail(FetchTripQuery query) {
        TripAggregate trip = tripAggregateRepositoryPort.findById(query.tripId())
                .orElseThrow(() -> new BusinessException(
                        query.requestId(),
                        query.requestDateTime(),
                        query.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(TRIP_NOT_FOUND, query.tripId()))
                ));

        RouteAggregate route = routeAggregateRepositoryPort.findById(trip.getRouteId())
                .orElseThrow(() -> new BusinessException(
                        query.requestId(),
                        query.requestDateTime(),
                        query.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, query.tripId()))
                ));

        TripEnrichment enrichment = enrichRoutes(List.of(route.getId()), null);
        return toFetchTripDetail(route, trip, enrichment);
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


    @Override
    public FetchTripsResult fetchTrips(FetchTripsQuery query) {
        PageInfo pageInfo = validatePageContext(query.context(), query.pageContext());
        // external is 1-based; Spring Data is 0-based
        PagedResult<TripFetchView> page = tripQueryPort.fetchTrips(
                query.merchantId(),
                query.merchantName(),
                pageInfo.pageNumber() - 1,
                pageInfo.pageSize()
        );

        List<TripFetchView> routes = page.getItems();
        TripEnrichment enrichment = enrichRoutes(
                routes.stream().map(TripFetchView::getId).toList(),
                query.merchantId()
        );

        List<FetchTripResult> items = routes.stream()
                .map(route -> toFetchRouteItem(route, enrichment))
                .collect(Collectors.toList());

        return FetchTripsResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private SearchWindow resolveSearchWindow(SearchTripQuery query) {
        LocalDate departureDate = DateTimeUtils.parseDateOrThrow(query.departureDate(), "departureDate",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        OffsetDateTime start = TripSpecification.dayStart(departureDate, DEFAULT_ZONE);
        OffsetDateTime endExclusive = TripSpecification.dayEndExclusive(departureDate, DEFAULT_ZONE);

        return new SearchWindow(start, endExclusive);
    }

    private TripEnrichment enrichRoutes(List<String> tripIds, String merchantId) {
        if (tripIds.isEmpty()) {
            return new TripEnrichment(Map.of(), Map.of(), Map.of(), Map.of());
        }

        Map<String, Long> seatAvailable = routeSeatAvailabilityPort.countAvailableSeats(tripIds);
        Map<String, TripAssignmentRecord> assignments = merchantId == null || merchantId.isBlank()
                ? tripAssignmentRepositoryPort.findLatestActiveByTripIds(tripIds)
                : tripAssignmentRepositoryPort.findLatestActiveByTripIds(tripIds, merchantId);
        List<String> vehicleIds = assignments.values().stream()
                .map(TripAssignmentRecord::getVehicleId)
                .distinct()
                .toList();
        Map<String, VehicleSnapshot> vehicles;
        if (vehicleIds.isEmpty()) {
            vehicles = Map.of();
        } else if (merchantId == null || merchantId.isBlank()) {
            vehicles = routeVehicleRepositoryPort.findByIds(vehicleIds);
        } else {
            vehicles = routeVehicleRepositoryPort.findByIds(vehicleIds, merchantId);
        }
        Map<String, List<RouteStopPlan>> stopsByRouteId = routePointRepositoryPort.findByRouteIds(tripIds);

        return new TripEnrichment(assignments, seatAvailable, vehicles, stopsByRouteId);
    }

    private SearchTripItemResult toSearchRouteItem(
            TripSearchView trip,
            TripEnrichment enrichment,
            Map<String, String> merchantNames
    ) {
        TripAssignmentRecord assignment = enrichment.assignments().get(trip.getId());
        VehicleSnapshot vehicle = findVehicle(assignment, enrichment);
        return SearchTripItemResult.builder()
                .id(trip.getId())
                .merchantId(trip.getMerchantId())
                .vehicleId(assignment.getVehicleId())
                .driverId(assignment.getDriverId())
                .ticketPrice(assignment.getTicketPrice())
                .merchantName(merchantNames.get(trip.getMerchantId()))
                .tripCode(trip.getTripCode())
                .pickupBranch(trip.getPickupBranch())
                .originCode(trip.getOriginCode())
                .originName(trip.getOriginName())
                .destinationName(trip.getDestinationName())
                .destinationCode(trip.getDestinationCode())
                .availableSeats(enrichment.seatAvailable().getOrDefault(trip.getId(), 0L))
                .departureTime(trip.getDepartureTime())
                .rawDepartureTime(trip.getRawDepartureTime())
                .rawDepartureDate(trip.getRawDepartureDate())
                .vehiclePlate(vehicle == null ? null : vehicle.getVehiclePlate())
                .hasFloor(vehicle != null && vehicle.isHasFloor())
                .routePoints(toRoutePoints(enrichment.stopsByRouteId().get(trip.getRouteId())))
                .build();
    }

    private String findMerchantName(String merchantId) {
        if (merchantId == null || merchantId.isBlank()) {
            return null;
        }
        return merchantRepositoryPort.findById(merchantId)
                .map(merchant -> merchant.getDisplayName() != null && !merchant.getDisplayName().isBlank()
                        ? merchant.getDisplayName()
                        : merchant.getLegalName())
                .orElse(null);
    }

    private PageInfo validatePageContext(RequestContext context, PageContext query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                context.requestId(), context.requestDateTime(), context.channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                context.requestId(), context.requestDateTime(), context.channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(context.requestId(), context.requestDateTime(), context.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(context.requestId(), context.requestDateTime(), context.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        return new PageInfo(pageSize, pageNumber);
    }

    private FetchTripResult toFetchRouteItem(TripFetchView trip, TripEnrichment enrichment) {
        TripAssignmentRecord assignment = enrichment.assignments().get(trip.getId());
        VehicleSnapshot vehicle = findVehicle(assignment, enrichment);

        return FetchTripResult.builder()
                .id(trip.getId())
                .creator(trip.getCreator())
                .pickupBranch(trip.getPickupBranch())
                .tripCode(trip.getTripCode())
                .originCode(trip.getOriginCode())
                .originName(trip.getOriginName())
                .destinationCode(trip.getDestinationCode())
                .destinationName(trip.getDestinationName())
                .departureTime(trip.getDepartureTime())
                .status(trip.getStatus())
                .vehicleId(assignment == null ? null : assignment.getVehicleId())
                .vehiclePlate(vehicle == null ? null : vehicle.getVehiclePlate())
                .hasFloor(vehicle != null && vehicle.isHasFloor())
                .assignedAt(assignment == null ? null : assignment.getAssignedAt())
                .routePoints(toRoutePoints(enrichment.stopsByRouteId().get(trip.getRouteId())))
                .build();
    }

    private FetchTripResult toFetchTripDetail(RouteAggregate route, TripAggregate trip, TripEnrichment enrichment) {
        TripAssignmentRecord assignment = enrichment.assignments().get(trip.getId());
        VehicleSnapshot vehicle = findVehicle(assignment, enrichment);

        return FetchTripResult.builder()
                .id(route.getId())
                .creator(route.getCreator())
                .pickupBranch(trip.getPickupBranch())
                .tripCode(trip.getTripCode())
                .originName(route.getOriginName())
                .originCode(route.getOriginCode())
                .destinationName(route.getDestinationName())
                .destinationCode(route.getDestinationCode())
                .departureTime(trip.getDepartureTime())
                .rawDepartureDate(trip.getRawDepartureDate())
                .rawDepartureTime(trip.getRawDepartureTime())
                .status(trip.getStatus() == null ? null : trip.getStatus())
                .vehicleId(assignment == null ? null : assignment.getVehicleId())
                .vehiclePlate(vehicle == null ? null : vehicle.getVehiclePlate())
                .hasFloor(vehicle != null && vehicle.isHasFloor())
                .assignedAt(assignment == null ? null : assignment.getAssignedAt())
                .routePoints(toRoutePoints(enrichment.stopsByRouteId().get(route.getId())))
                .build();
    }

    private VehicleSnapshot findVehicle(TripAssignmentRecord assignment, TripEnrichment enrichment) {
        return assignment == null ? null : enrichment.vehicles().get(assignment.getVehicleId());
    }

    private List<RoutePointResult> toRoutePoints(List<RouteStopPlan> stops) {
        return (stops == null ? List.<RouteStopPlan>of() : stops).stream()
                .map(this::toRoutePoint)
                .toList();
    }


    private record TripEnrichment(
            Map<String, TripAssignmentRecord> assignments,
            Map<String, Long> seatAvailable,
            Map<String, VehicleSnapshot> vehicles,
            Map<String, List<RouteStopPlan>> stopsByRouteId
    ) {
    }
}
