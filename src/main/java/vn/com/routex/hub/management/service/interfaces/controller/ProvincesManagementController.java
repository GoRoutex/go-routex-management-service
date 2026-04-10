package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceResult;
import vn.com.routex.hub.management.service.application.services.ProvincesManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.provinces.CreateProvinceRequest;
import vn.com.routex.hub.management.service.interfaces.models.provinces.CreateProvinceResponse;
import vn.com.routex.hub.management.service.interfaces.models.provinces.DeleteProvinceRequest;
import vn.com.routex.hub.management.service.interfaces.models.provinces.DeleteProvinceResponse;
import vn.com.routex.hub.management.service.interfaces.models.provinces.FetchProvincesResponse;
import vn.com.routex.hub.management.service.interfaces.models.provinces.SearchProvincesResponse;
import vn.com.routex.hub.management.service.interfaces.models.provinces.UpdateProvinceRequest;
import vn.com.routex.hub.management.service.interfaces.models.provinces.UpdateProvinceResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.PROVINCES_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
public class ProvincesManagementController {

    private final ApiResultFactory apiResultFactory;
    private final ProvincesManagementService provincesManagementService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @GetMapping(PROVINCES_SERVICE + SEARCH_PATH)
    @PreAuthorize("hasAuthority('provinces:management') or hasRole('ADMIN')")
    public ResponseEntity<SearchProvincesResponse> searchProvinces(
            HttpServletRequest servletRequest,
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, ApiRequestUtils.getBaseRequestOrDefault(servletRequest));
        SearchProvincesResult result = provincesManagementService.searchProvinces(SearchProvincesQuery.builder()
                .merchantId(merchantId)
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

    @PostMapping(PROVINCES_SERVICE + CREATE_PATH)
    @PreAuthorize("hasAuthority('provinces:management') or hasRole('ADMIN')")
    public ResponseEntity<CreateProvinceResponse> createProvince(@Valid @RequestBody CreateProvinceRequest request,
                                                                 HttpServletRequest servletRequest) {
        sLog.info("[PROVINCE-MANAGEMENT] Create Province Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        CreateProvinceResult result = provincesManagementService.createProvince(CreateProvinceCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .name(request.getData().getName())
                .code(request.getData().getCode())
                .build());


        CreateProvinceResponse response = CreateProvinceResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(CreateProvinceResponse.CreateProvinceResponseData.builder()
                        .id(result.id())
                        .name(result.name())
                        .code(result.code())
                        .build())
                .build();
        sLog.info("[PROVINCE-MANAGEMENT] Create Province Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(PROVINCES_SERVICE + UPDATE_PATH)
    @PreAuthorize("hasAuthority('provinces:management') or hasRole('ADMIN')")
    public ResponseEntity<UpdateProvinceResponse> updateProvince(@Valid @RequestBody UpdateProvinceRequest request,
                                                                 HttpServletRequest servletRequest) {
        sLog.info("[PROVINCE-MANAGEMENT] Update Province Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        UpdateProvinceResult result = provincesManagementService.updateProvince(UpdateProvinceCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .id(request.getData().getId())
                .name(request.getData().getName())
                .code(request.getData().getCode())
                .build());


        UpdateProvinceResponse response = UpdateProvinceResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(UpdateProvinceResponse.UpdateProvinceResponseData.builder()
                        .id(result.id())
                        .name(result.name())
                        .code(result.code())
                        .build())
                .build();

        sLog.info("[PROVINCE-MANAGEMENT] Update Province Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(PROVINCES_SERVICE + DELETE_PATH)
    @PreAuthorize("hasAuthority('provinces:management') or hasRole('ADMIN')")
    public ResponseEntity<DeleteProvinceResponse> deleteProvince(@Valid @RequestBody DeleteProvinceRequest request,
                                                                 HttpServletRequest servletRequest) {
        sLog.info("[PROVINCE-MANAGEMENT] Delete Province: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        DeleteProvinceResult result = provincesManagementService.deleteProvince(DeleteProvinceCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .id(request.getData().getId())
                .build());


        DeleteProvinceResponse response = DeleteProvinceResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(DeleteProvinceResponse.DeleteProvinceResponseData.builder()
                        .id(result.id())
                        .build())
                .build();

        sLog.info("[PROVINCE-MANAGEMENT] Delete Province: {}", response);
        return HttpUtils.buildResponse(request, response);
    }


    @GetMapping(PROVINCES_SERVICE + FETCH_PATH)
    @PreAuthorize("hasAuthority('provinces:management') or hasRole('ADMIN')")
    public ResponseEntity<FetchProvincesResponse> fetchProvinces(
            HttpServletRequest servletRequest,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchProvincesResult result = provincesManagementService.fetchProvinces(
                FetchProvincesQuery.builder()
                        .context(HttpUtils.toContext(baseRequest, merchantId))
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
