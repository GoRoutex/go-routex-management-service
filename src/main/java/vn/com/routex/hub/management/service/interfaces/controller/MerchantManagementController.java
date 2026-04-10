package vn.com.routex.hub.management.service.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsResult;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantCommand;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantResult;
import vn.com.routex.hub.management.service.application.services.MerchantManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.merchant.FetchMerchantsResponse;
import vn.com.routex.hub.management.service.interfaces.models.merchant.UpdateMerchantRequest;
import vn.com.routex.hub.management.service.interfaces.models.merchant.UpdateMerchantResponse;

import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
//@PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
public class MerchantManagementController {

    private final MerchantManagementService merchantManagementService;
    private final ApiResultFactory apiResultFactory;

    @GetMapping(MERCHANT_SERVICE + FETCH_PATH)
    public ResponseEntity<FetchMerchantsResponse> fetchMerchants(
            HttpServletRequest servletRequest,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        FetchMerchantsResult result = merchantManagementService.fetchMerchants(FetchMerchantsQuery.builder()
                .context(HttpUtils.toContext(baseRequest))
                .pageNumber(String.valueOf(pageNumber))
                .pageSize(String.valueOf(pageSize))
                .build());

        FetchMerchantsResponse response = FetchMerchantsResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(FetchMerchantsResponse.FetchMerchantsResponsePage.builder()
                        .items(result.items().stream()
                                .map(item -> FetchMerchantsResponse.FetchMerchantResponseData.builder()
                                        .id(item.id())
                                        .code(item.code())
                                        .name(item.name())
                                        .taxCode(item.taxCode())
                                        .phone(item.phone())
                                        .email(item.email())
                                        .address(item.address())
                                        .representativeName(item.representativeName())
                                        .commissionRate(item.commissionRate())
                                        .status(item.status())
                                        .build())
                                .collect(Collectors.toList()))
                        .totalPartners(result.totalPartners())
                        .totalRevenueShare(result.totalRevenueShare())
                        .avgRating(result.avgRating())
                        .numberOfPendingApps(result.numberOfPendingApps())
                        .pagination(FetchMerchantsResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @PostMapping(MERCHANT_SERVICE + UPDATE_PATH)
    public ResponseEntity<UpdateMerchantResponse> updateMerchant(
            @Valid @RequestBody UpdateMerchantRequest request
    ) {
        UpdateMerchantResult result = merchantManagementService.updateMerchant(UpdateMerchantCommand.builder()
                .context(HttpUtils.toContext(request))
                .merchantId(request.getData().getMerchantId())
                .updatedBy(request.getData().getUpdatedBy())
                .name(request.getData().getName())
                .taxCode(request.getData().getTaxCode())
                .phone(request.getData().getPhone())
                .email(request.getData().getEmail())
                .address(request.getData().getAddress())
                .representativeName(request.getData().getRepresentativeName())
                .commissionRate(request.getData().getCommissionRate())
                .status(request.getData().getStatus())
                .build());

        UpdateMerchantResponse response = UpdateMerchantResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(UpdateMerchantResponse.UpdateMerchantResponseData.builder()
                        .id(result.id())
                        .code(result.code())
                        .name(result.name())
                        .taxCode(result.taxCode())
                        .phone(result.phone())
                        .email(result.email())
                        .address(result.address())
                        .representativeName(result.representativeName())
                        .commissionRate(result.commissionRate())
                        .status(result.status())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }
}
