package vn.com.routex.hub.management.service.infrastructure.persistence.utils;


import lombok.experimental.UtilityClass;
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;

@UtilityClass
public class ExceptionUtils {

    public ApiResult buildResultResponse(String responseCode, String description) {
        return ApiResult
                .builder()
                .responseCode(responseCode)
                .description(description)
                .build();
    }
}
