package vn.com.routex.hub.management.service.application.services.impl;

import org.junit.jupiter.api.Test;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;
import vn.com.routex.hub.management.service.application.command.operationpoint.FetchOperationPointQuery;
import vn.com.routex.hub.management.service.application.command.operationpoint.FetchOperationPointResult;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.operationpoint.model.OperationPoint;
import vn.com.routex.hub.management.service.domain.operationpoint.port.OperationPointRepositoryPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OperationPointManagementServiceImplPagingTest {

    @Test
    void fetchOperationPoint_usesOneBasedPageNumberExternally() {
        OperationPointRepositoryPort repo = mock(OperationPointRepositoryPort.class);
        OperationPointManagementServiceImpl service = new OperationPointManagementServiceImpl(repo);

        when(repo.fetch(0, 10)).thenReturn(PagedResult.<OperationPoint>builder()
                .items(List.of())
                .pageNumber(0)
                .pageSize(10)
                .totalElements(11)
                .totalPages(2)
                .build());

        FetchOperationPointResult result = service.fetchOperationPoint(FetchOperationPointQuery.builder()
                .context(RequestContext.builder()
                        .requestId("req-1")
                        .requestDateTime("2026-04-02T00:00:00.000+07:00")
                        .channel("ONL")
                        .build())
                .pageNumber("1")
                .pageSize("10")
                .build());

        verify(repo).fetch(0, 10);
        assertEquals(1, result.pageNumber());
        assertEquals(10, result.pageSize());
        assertEquals(11, result.totalElements());
        assertEquals(2, result.totalPages());
    }

    @Test
    void fetchOperationPoint_defaultsToPage1AndSize10WhenMissing() {
        OperationPointRepositoryPort repo = mock(OperationPointRepositoryPort.class);
        OperationPointManagementServiceImpl service = new OperationPointManagementServiceImpl(repo);

        when(repo.fetch(0, 10)).thenReturn(PagedResult.<OperationPoint>builder()
                .items(List.of())
                .pageNumber(0)
                .pageSize(10)
                .totalElements(0)
                .totalPages(0)
                .build());

        FetchOperationPointResult result = service.fetchOperationPoint(FetchOperationPointQuery.builder()
                .context(RequestContext.builder()
                        .requestId("req-2")
                        .requestDateTime("2026-04-02T00:00:00.000+07:00")
                        .channel("ONL")
                        .build())
                .pageNumber(null)
                .pageSize(null)
                .build());

        verify(repo).fetch(0, 10);
        assertEquals(1, result.pageNumber());
        assertEquals(10, result.pageSize());
    }
}

