package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.management.service.application.command.seat.SearchSeatCommand;
import vn.com.routex.hub.management.service.application.command.seat.SearchSeatResult;
import vn.com.routex.hub.management.service.application.services.RouteSeatService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.seat.SearchSeatResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEAT_DIAGRAM;

@RestController
@RequestMapping( API_PATH + API_VERSION + MANAGEMENT_PATH + SEAT_DIAGRAM)
@RequiredArgsConstructor
public class RouteSeatServiceController {

    private final RouteSeatService routeSeatService;
    private final ApiResultFactory apiResultFactory;

    @GetMapping(SEARCH_PATH)
    public ResponseEntity<SearchSeatResponse> searchSeat(@RequestParam(defaultValue = "1") int pageNumber,
                                                         @RequestParam(defaultValue = "10") int pageSize,
                                                         @RequestParam String routeId,
                                                         HttpServletRequest servletRequest) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        SearchSeatResult result = routeSeatService.searchSeat(SearchSeatCommand.builder()
                .context(HttpUtils.toContext(baseRequest))
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .routeId(routeId)
                .build());


        List<SearchSeatResponse.SearchSeatResponseData> seatDataList = result.data()
                .stream()
                .map(r -> SearchSeatResponse.SearchSeatResponseData.builder()
                        .seatId(r.seatId())
                        .code(r.code())
                        .status(r.status())
                        .floor(r.floor())
                        .rowNo(r.rowNo())
                        .colNo(r.colNo())
                        .build())
                .collect(Collectors.toList());

        SearchSeatResponse response = SearchSeatResponse
                .builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(SearchSeatResponse.SearchSeatResponsePage.builder()
                        .items(seatDataList)
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }
}
