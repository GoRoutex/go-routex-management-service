package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.common.PageContext;
import vn.com.routex.hub.management.service.application.command.common.PageInfo;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;
import vn.com.routex.hub.management.service.application.command.route.FetchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.FetchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesResult;
import vn.com.routex.hub.management.service.application.command.route.RoutePointResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteItemResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.SearchWindow;
import vn.com.routex.hub.management.service.application.services.RouteManagementService;
import vn.com.routex.hub.management.service.application.specification.RouteSpecification;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RoutePointRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteQueryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteFetchView;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteSearchView;
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

@Service
@RequiredArgsConstructor
public class RouteManagementServiceImpl implements RouteManagementService {

    private final RoutePointRepositoryPort routePointRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RouteVehicleRepositoryPort routeVehicleRepositoryPort;
    private final RouteSeatAvailabilityPort routeSeatAvailabilityPort;
    private final RouteQueryPort routeQueryPort;
    private final MerchantRepositoryPort merchantRepositoryPort;

    @Override
    public SearchRouteResult searchRoute(SearchRouteQuery query) {
        PageInfo pageInfo = validatePageContext(query.context(), query.pageContext());
        SearchWindow searchWindow = resolveSearchWindow(query);

        List<RouteSearchView> searchedRoutes = routeQueryPort.searchAssignedRoutes(
                null,
                query.origin(),
                query.destination(),
                searchWindow.start(),
                searchWindow.endExclusive(),
                pageInfo.pageNumber() - 1, // external is 1-based; Spring Data is 0-based
                pageInfo.pageSize()
        );

        RouteEnrichment enrichment = enrichRoutes(
                searchedRoutes.stream().map(RouteSearchView::getId).toList(),
                null
        );

        Map<String, String> merchantNames = searchedRoutes.stream()
                .map(RouteSearchView::getMerchantId)
                .distinct()
                .collect(Collectors.toMap(
                        merchantId -> merchantId,
                        this::findMerchantName,
                        (left, right) -> left
                ));

        List<SearchRouteItemResult> items = searchedRoutes.stream()
                .map(route -> toSearchRouteItem(route, enrichment, merchantNames))
                .collect(Collectors.toList());

        return SearchRouteResult.builder()
                .data(items)
                .build();
    }

    @Override
    public FetchRouteResult fetchRouteDetail(FetchRouteQuery query) {
        var route = routeAggregateRepositoryPort.findById(query.routeId())
                .orElseThrow(() -> new BusinessException(
                        query.requestId(),
                        query.requestDateTime(),
                        query.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, query.routeId()))
                ));

        RouteEnrichment enrichment = enrichRoutes(List.of(route.getId()), null);
        return toFetchRouteDetail(route, enrichment);
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
    public FetchRoutesResult fetchRoutes(FetchRoutesQuery query) {
        PageInfo pageInfo = validatePageContext(query.context(), query.pageContext());
        // external is 1-based; Spring Data is 0-based
        PagedResult<RouteFetchView> page = routeQueryPort.fetchRoutes(
                query.merchantId(),
                query.merchantName(),
                pageInfo.pageNumber() - 1,
                pageInfo.pageSize()
        );

        List<RouteFetchView> routes = page.getItems();
        RouteEnrichment enrichment = enrichRoutes(
                routes.stream().map(RouteFetchView::getId).toList(),
                query.merchantId()
        );

        List<FetchRouteResult> items = routes.stream()
                .map(route -> toFetchRouteItem(route, enrichment))
                .collect(Collectors.toList());

        return FetchRoutesResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private SearchWindow resolveSearchWindow(SearchRouteQuery query) {
        LocalDate departureDate = DateTimeUtils.parseDateOrThrow(query.departureDate(), "departureDate",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        OffsetDateTime start = RouteSpecification.dayStart(departureDate, DEFAULT_ZONE);
        OffsetDateTime endExclusive = RouteSpecification.dayEndExclusive(departureDate, DEFAULT_ZONE);

        return new SearchWindow(start, endExclusive);
    }

    private RouteEnrichment enrichRoutes(List<String> routeIds, String merchantId) {
        if (routeIds.isEmpty()) {
            return new RouteEnrichment(Map.of(), Map.of(), Map.of(), Map.of());
        }

        Map<String, Long> seatAvailable = routeSeatAvailabilityPort.countAvailableSeats(routeIds);
        Map<String, RouteAssignmentRecord> assignments = merchantId == null || merchantId.isBlank()
                ? routeAssignmentRepositoryPort.findLatestActiveByRouteIds(routeIds)
                : routeAssignmentRepositoryPort.findLatestActiveByRouteIds(routeIds, merchantId);
        List<String> vehicleIds = assignments.values().stream()
                .map(RouteAssignmentRecord::getVehicleId)
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
        Map<String, List<RouteStopPlan>> stopsByRouteId = routePointRepositoryPort.findByRouteIds(routeIds);

        return new RouteEnrichment(assignments, seatAvailable, vehicles, stopsByRouteId);
    }

    private SearchRouteItemResult toSearchRouteItem(
            RouteSearchView route,
            RouteEnrichment enrichment,
            Map<String, String> merchantNames
    ) {
        RouteAssignmentRecord assignment = enrichment.assignments().get(route.getId());
        VehicleSnapshot vehicle = findVehicle(assignment, enrichment);

        return SearchRouteItemResult.builder()
                .id(route.getId())
                .merchantId(route.getMerchantId())
                .merchantName(merchantNames.get(route.getMerchantId()))
                .routeCode(route.getRouteCode())
                .pickupBranch(route.getPickupBranch())
                .origin(route.getOrigin())
                .destination(route.getDestination())
                .availableSeats(enrichment.seatAvailable().getOrDefault(route.getId(), 0L))
                .plannedStartTime(route.getPlannedStartTime())
                .plannedEndTime(route.getPlannedEndTime())
                .vehiclePlate(vehicle == null ? null : vehicle.getVehiclePlate())
                .hasFloor(vehicle != null && vehicle.isHasFloor())
                .routePoints(toRoutePoints(enrichment.stopsByRouteId().get(route.getId())))
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

    private FetchRouteResult toFetchRouteItem(RouteFetchView route, RouteEnrichment enrichment) {
        RouteAssignmentRecord assignment = enrichment.assignments().get(route.getId());
        VehicleSnapshot vehicle = findVehicle(assignment, enrichment);

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
                .status(route.getStatus())
                .availableSeats(enrichment.seatAvailable().getOrDefault(route.getId(), 0L))
                .vehicleId(assignment == null ? null : assignment.getVehicleId())
                .vehiclePlate(vehicle == null ? null : vehicle.getVehiclePlate())
                .hasFloor(vehicle != null && vehicle.isHasFloor())
                .assignedAt(assignment == null ? null : assignment.getAssignedAt())
                .routePoints(toRoutePoints(enrichment.stopsByRouteId().get(route.getId())))
                .build();
    }

    private FetchRouteResult toFetchRouteDetail(vn.com.routex.hub.management.service.domain.route.model.RouteAggregate route, RouteEnrichment enrichment) {
        RouteAssignmentRecord assignment = enrichment.assignments().get(route.getId());
        VehicleSnapshot vehicle = findVehicle(assignment, enrichment);

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
                .status(route.getStatus() == null ? null : route.getStatus().name())
                .availableSeats(enrichment.seatAvailable().getOrDefault(route.getId(), 0L))
                .vehicleId(assignment == null ? null : assignment.getVehicleId())
                .vehiclePlate(vehicle == null ? null : vehicle.getVehiclePlate())
                .hasFloor(vehicle != null && vehicle.isHasFloor())
                .assignedAt(assignment == null ? null : assignment.getAssignedAt())
                .routePoints(toRoutePoints(enrichment.stopsByRouteId().get(route.getId())))
                .build();
    }

    private VehicleSnapshot findVehicle(RouteAssignmentRecord assignment, RouteEnrichment enrichment) {
        return assignment == null ? null : enrichment.vehicles().get(assignment.getVehicleId());
    }

    private List<RoutePointResult> toRoutePoints(List<RouteStopPlan> stops) {
        return (stops == null ? List.<RouteStopPlan>of() : stops).stream()
                .map(this::toRoutePoint)
                .toList();
    }


    private record RouteEnrichment(
            Map<String, RouteAssignmentRecord> assignments,
            Map<String, Long> seatAvailable,
            Map<String, VehicleSnapshot> vehicles,
            Map<String, List<RouteStopPlan>> stopsByRouteId
    ) {
    }
}
