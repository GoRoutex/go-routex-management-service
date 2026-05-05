package vn.com.routex.hub.management.service.infrastructure.integration.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.routex.hub.management.service.infrastructure.integration.common.config.InternalServiceFeignConfig;
import vn.com.routex.hub.management.service.infrastructure.integration.userservice.model.UserServiceFetchCustomersRequest;
import vn.com.routex.hub.management.service.infrastructure.integration.userservice.model.UserServiceInternalModels;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@FeignClient(
        name = "userServiceInternalClient",
        url = "${clients.user-service.base-url}",
        configuration = InternalServiceFeignConfig.class
)
public interface UserServiceInternalClient {

    @GetMapping("/customers/detail-by-user-id")
    BaseResponse<UserServiceInternalModels.CustomerData> fetchCustomerByUserId(@RequestParam String userId);

    @PostMapping("/customers/fetch-by-user-ids")
    BaseResponse<UserServiceInternalModels.CustomerListData> fetchCustomersByUserIds(
            @RequestBody UserServiceFetchCustomersRequest request
    );
}
