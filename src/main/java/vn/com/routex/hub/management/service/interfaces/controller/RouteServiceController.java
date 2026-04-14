package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.common.PageContext;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteResult;
import vn.com.routex.hub.management.service.application.services.RouteManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.mapper.RouteResponseMapper;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.FetchRouteResponse;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchRouteResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ROUTE_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH + ROUTE_SERVICE)
@RequiredArgsConstructor
public class RouteServiceController {

    private final RouteManagementService routeManagementService;
    private final ApiResultFactory apiResultFactory;
    private final RouteResponseMapper routeResponseMapper;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @PostMapping(SEARCH_PATH)
    public ResponseEntity<SearchRouteResponse> searchRoute(@Valid @RequestBody SearchRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[ROUTE-SERVICE] Search Route Request: {}", request);
        String merchantId = ApiRequestUtils.getMerchantId(servletRequest);
        SearchRouteResult result = routeManagementService.searchRoute(SearchRouteQuery.builder()

                .merchantId(merchantId)
                .origin(request.getData().getOrigin())
                .destination(request.getData().getDestination())
                .departureDate(request.getData().getDepartureDate())
                .seat(request.getData().getSeat())
                .fromTime(request.getData().getFromTime())
                .toTime(request.getData().getToTime())
                .pageContext(PageContext.builder()
                        .pageSize(request.getData().getPageSize())
                        .pageNumber(request.getData().getPageNumber())
                        .build())
                .context(HttpUtils.toContext(request))
                .build());

        SearchRouteResponse response = SearchRouteResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(result.data().stream()
                        .map(routeResponseMapper::toSearchRouteResponseData)
                        .toList())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(FETCH_PATH)
    public ResponseEntity<FetchRouteResponse> fetchRoutes(
            HttpServletRequest servletRequest,
            @RequestParam(required = false) String merchantName,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.getMerchantId(servletRequest);
        FetchRoutesResult result = routeManagementService.fetchRoutes(FetchRoutesQuery.builder()
                .pageContext(PageContext.builder()
                        .pageNumber(String.valueOf(pageNumber))
                        .pageSize(String.valueOf(pageSize))
                        .build())
                .merchantId(merchantId)
                .merchantName(merchantName)
                .context(HttpUtils.toContext(baseRequest))
                .build());


        FetchRouteResponse response = FetchRouteResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(FetchRouteResponse.FetchRouteResponsePage.builder()
                        .items(result.items().stream()
                                .map(routeResponseMapper::toPublicFetchRouteResponseData)
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
