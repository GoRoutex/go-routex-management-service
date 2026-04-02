package vn.com.routex.hub.management.service.application.services.impl;

import org.junit.jupiter.api.Test;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesResult;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.route.port.RoutePointRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteProvincesLookupPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteQueryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSaleEventPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteSeatAvailabilityPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteFetchView;
import vn.com.routex.hub.management.service.domain.operationpoint.port.OperationPointRepositoryPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RouteManagementServiceImplPagingTest {

    @Test
    void fetchRoutes_usesOneBasedPageNumberExternally() {
        RouteAggregateRepositoryPort routeAggregateRepositoryPort = mock(RouteAggregateRepositoryPort.class);
        RoutePointRepositoryPort routePointRepositoryPort = mock(RoutePointRepositoryPort.class);
        RouteAssignmentRepositoryPort routeAssignmentRepositoryPort = mock(RouteAssignmentRepositoryPort.class);
        RouteVehicleRepositoryPort routeVehicleRepositoryPort = mock(RouteVehicleRepositoryPort.class);
        RouteProvincesLookupPort routeProvincesLookupPort = mock(RouteProvincesLookupPort.class);
        RouteSeatAvailabilityPort routeSeatAvailabilityPort = mock(RouteSeatAvailabilityPort.class);
        RouteQueryPort routeQueryPort = mock(RouteQueryPort.class);
        RouteSaleEventPort routeSaleEventPort = mock(RouteSaleEventPort.class);
        OperationPointRepositoryPort operationPointRepositoryPort = mock(OperationPointRepositoryPort.class);

        RouteManagementServiceImpl service = new RouteManagementServiceImpl(
                routeAggregateRepositoryPort,
                routePointRepositoryPort,
                routeAssignmentRepositoryPort,
                routeVehicleRepositoryPort,
                routeProvincesLookupPort,
                routeSeatAvailabilityPort,
                routeQueryPort,
                routeSaleEventPort,
                operationPointRepositoryPort
        );

        when(routeQueryPort.fetchRoutes(0, 10)).thenReturn(PagedResult.<RouteFetchView>builder()
                .items(List.of())
                .pageNumber(0)
                .pageSize(10)
                .totalElements(11)
                .totalPages(2)
                .build());

        FetchRoutesResult result = service.fetchRoutes(FetchRoutesQuery.builder()
                .pageNumber("1")
                .pageSize("10")
                .requestId("req-1")
                .requestDateTime("2026-04-02T00:00:00.000+07:00")
                .channel("ONL")
                .build());

        verify(routeQueryPort).fetchRoutes(0, 10);
        assertEquals(1, result.pageNumber());
        assertEquals(10, result.pageSize());
        assertEquals(11, result.totalElements());
        assertEquals(2, result.totalPages());
    }

    @Test
    void fetchRoutes_defaultsToPage1AndSize10WhenMissing() {
        RouteAggregateRepositoryPort routeAggregateRepositoryPort = mock(RouteAggregateRepositoryPort.class);
        RoutePointRepositoryPort routePointRepositoryPort = mock(RoutePointRepositoryPort.class);
        RouteAssignmentRepositoryPort routeAssignmentRepositoryPort = mock(RouteAssignmentRepositoryPort.class);
        RouteVehicleRepositoryPort routeVehicleRepositoryPort = mock(RouteVehicleRepositoryPort.class);
        RouteProvincesLookupPort routeProvincesLookupPort = mock(RouteProvincesLookupPort.class);
        RouteSeatAvailabilityPort routeSeatAvailabilityPort = mock(RouteSeatAvailabilityPort.class);
        RouteQueryPort routeQueryPort = mock(RouteQueryPort.class);
        RouteSaleEventPort routeSaleEventPort = mock(RouteSaleEventPort.class);
        OperationPointRepositoryPort operationPointRepositoryPort = mock(OperationPointRepositoryPort.class);

        RouteManagementServiceImpl service = new RouteManagementServiceImpl(
                routeAggregateRepositoryPort,
                routePointRepositoryPort,
                routeAssignmentRepositoryPort,
                routeVehicleRepositoryPort,
                routeProvincesLookupPort,
                routeSeatAvailabilityPort,
                routeQueryPort,
                routeSaleEventPort,
                operationPointRepositoryPort
        );

        when(routeQueryPort.fetchRoutes(0, 10)).thenReturn(PagedResult.<RouteFetchView>builder()
                .items(List.of())
                .pageNumber(0)
                .pageSize(10)
                .totalElements(0)
                .totalPages(0)
                .build());

        FetchRoutesResult result = service.fetchRoutes(FetchRoutesQuery.builder()
                .pageNumber(null)
                .pageSize(null)
                .requestId("req-2")
                .requestDateTime("2026-04-02T00:00:00.000+07:00")
                .channel("ONL")
                .build());

        verify(routeQueryPort).fetchRoutes(0, 10);
        assertEquals(1, result.pageNumber());
        assertEquals(10, result.pageSize());
    }
}
