package vn.com.routex.hub.management.service.infrastructure.integration.merchantplatform.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.management.service.infrastructure.integration.common.config.InternalServiceFeignConfig;
import vn.com.routex.hub.management.service.infrastructure.integration.merchantplatform.model.MerchantPlatformFetchMerchantsRequest;
import vn.com.routex.hub.management.service.infrastructure.integration.merchantplatform.model.MerchantPlatformInternalModels;
import vn.com.routex.hub.management.service.infrastructure.integration.merchantplatform.model.MerchantPlatformUpdateMerchantRequest;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@FeignClient(
        name = "merchantPlatformInternalClient",
        url = "${clients.merchant-platform.base-url}",
        configuration = InternalServiceFeignConfig.class
)
public interface MerchantPlatformInternalClient {

    @GetMapping("/api/v1/internal/merchant-service/detail")
    BaseResponse<MerchantPlatformInternalModels.MerchantData> fetchMerchantDetail(@RequestParam String merchantId);

    @GetMapping("/api/v1/internal/merchant-service/fetch")
    BaseResponse<MerchantPlatformInternalModels.MerchantPage> fetchMerchants(
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    );

    @PostMapping("/api/v1/internal/merchant-service/fetch-by-ids")
    BaseResponse<java.util.List<MerchantPlatformInternalModels.MerchantData>> fetchMerchantsByIds(
            @RequestBody MerchantPlatformFetchMerchantsRequest request
    );

    @GetMapping("/api/v1/internal/merchant-service/search-ids")
    BaseResponse<java.util.List<String>> searchMerchantIds(@RequestParam String merchantName);

    @PostMapping("/api/v1/internal/merchant-service/update")
    BaseResponse<MerchantPlatformInternalModels.MerchantData> updateMerchant(
            @RequestBody MerchantPlatformUpdateMerchantRequest request
    );

    @GetMapping("/api/v1/internal/merchant-service/applications/fetch")
    BaseResponse<MerchantPlatformInternalModels.MerchantApplicationFormPage> fetchApplicationForms(
            @RequestParam(required = false) ApplicationFormStatus status,
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    );

    @GetMapping("/api/v1/internal/merchant-service/applications/detail")
    BaseResponse<MerchantPlatformInternalModels.MerchantApplicationFormData> fetchApplicationFormDetail(
            @RequestParam String applicationFormId
    );
}
