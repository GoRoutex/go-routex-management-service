package vn.com.routex.hub.management.service.interfaces.factory;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@Component
public class ApiResultFactory {
    public ApiResult buildSuccess() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }
}
