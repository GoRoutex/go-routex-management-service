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
import vn.com.routex.hub.management.service.application.command.route.FetchTripQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchTripResult;
import vn.com.routex.hub.management.service.application.command.route.FetchTripsQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchTripsResult;
import vn.com.routex.hub.management.service.application.command.route.SearchTripQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchTripResult;
import vn.com.routex.hub.management.service.application.services.TripManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.mapper.TripResponseMapper;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchTripRequest;
import vn.com.routex.hub.management.service.interfaces.models.route.SearchTripResponse;
import vn.com.routex.hub.management.service.interfaces.models.trip.FetchTripDetailResponse;
import vn.com.routex.hub.management.service.interfaces.models.trip.FetchTripResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.DETAIL_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.TRIP_SERVICE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH + TRIP_SERVICE)
@RequiredArgsConstructor
public class TripServiceController {

    private final TripManagementService tripManagementService;
    private final ApiResultFactory apiResultFactory;
    private final TripResponseMapper tripResponseMapper;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @PostMapping(SEARCH_PATH)
    public ResponseEntity<SearchTripResponse> searchRoute(@Valid @RequestBody SearchTripRequest request) {
        sLog.info("[TRIP-SERVICE] Search Trip Request: {}", request);
        SearchTripResult result = tripManagementService.searchTrip(SearchTripQuery.builder()
                .originName(request.getData().getOrigin())
                .destinationName(request.getData().getDestination())
                .originProvinceId(request.getData().getOriginProvinceId())
                .destinationProvinceId(request.getData().getDestinationProvinceId())
                .departureDate(request.getData().getDepartureDate())
                .seat(request.getData().getSeat())
                .pageContext(PageContext.builder()
                        .pageSize(request.getData().getPageSize())
                        .pageNumber(request.getData().getPageNumber())
                        .build())
                .context(HttpUtils.toContext(request))
                .build());

        SearchTripResponse response = SearchTripResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(result.data().stream()
                        .map(tripResponseMapper::toSearchTripResponseData)
                        .toList())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(DETAIL_PATH)
    public ResponseEntity<FetchTripDetailResponse> fetchRouteDetail(
            HttpServletRequest servletRequest,
            @RequestParam String tripId
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        sLog.info("[TRIP-SERVICE] Fetch Trip Detail Request tripId: {}", tripId);

        FetchTripResult result = tripManagementService.fetchTripDetail(FetchTripQuery.builder()
                .tripId(tripId)
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .build());

        FetchTripDetailResponse response = FetchTripDetailResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(tripResponseMapper.toFetchTripDetailResponseData(result))
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping(FETCH_PATH)
    public ResponseEntity<FetchTripResponse> fetchRoutes(
            HttpServletRequest servletRequest,
            @RequestParam(required = false) String merchantName,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.getMerchantId(servletRequest);
        FetchTripsResult result = tripManagementService.fetchTrips(FetchTripsQuery.builder()
                .pageContext(PageContext.builder()
                        .pageNumber(String.valueOf(pageNumber))
                        .pageSize(String.valueOf(pageSize))
                        .build())
                .merchantId(merchantId)
                .merchantName(merchantName)
                .context(HttpUtils.toContext(baseRequest))
                .build());


        FetchTripResponse response = FetchTripResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(FetchTripResponse.FetchTripResponsePage.builder()
                        .items(result.items().stream()
                                .map(tripResponseMapper::toPublicFetchTripResponseData)
                                .toList())

                        .pagination(FetchTripResponse.Pagination.builder()
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
