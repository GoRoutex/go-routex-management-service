package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;
import vn.com.routex.hub.management.service.application.services.ProvincesManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.provinces.FetchProvincesResponse;
import vn.com.routex.hub.management.service.interfaces.models.provinces.SearchProvincesResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.PROVINCES_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
public class ProvincesManagementController {

    private final ApiResultFactory apiResultFactory;
    private final ProvincesManagementService provincesManagementService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @GetMapping(PROVINCES_SERVICE + SEARCH_PATH)
    public ResponseEntity<SearchProvincesResponse> searchProvinces(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {
        SearchProvincesResult result = provincesManagementService.searchProvinces(SearchProvincesQuery.builder()
                .keyword(keyword)
                .page(page)
                .size(size)
                .build());

        SearchProvincesResponse response = SearchProvincesResponse.builder()
                .data(result.data().stream()
                        .map(item -> SearchProvincesResponse.SearchProvincesResponseData.builder()
                                .id(item.id())
                                .name(item.name())
                                .code(item.code())
                                .build())
                        .toList())
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping(PROVINCES_SERVICE + FETCH_PATH)
    @PreAuthorize("hasAuthority('provinces:management') or hasRole('ADMIN')")
    public ResponseEntity<FetchProvincesResponse> fetchProvinces(
            HttpServletRequest servletRequest,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        FetchProvincesResult result = provincesManagementService.fetchProvinces(
                FetchProvincesQuery.builder()
                        .context(HttpUtils.toContext(baseRequest))
                        .pageSize(String.valueOf(pageSize))
                        .pageNumber(String.valueOf(pageNumber))
                        .build()
        );

        List<FetchProvincesResponse.FetchProvincesResponseData> dataList = result.items().stream()
                .map(p -> FetchProvincesResponse.FetchProvincesResponseData.builder()
                        .id(p.id())
                        .name(p.name())
                        .code(p.code())
                        .build())
                .collect(Collectors.toList());

        FetchProvincesResponse response = FetchProvincesResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchProvincesResponse.FetchProvincesResponsePage.builder()
                        .items(dataList)
                        .pagination(FetchProvincesResponse.Pagination.builder()
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
