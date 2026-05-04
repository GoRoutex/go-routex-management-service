package vn.com.routex.hub.management.service.infrastructure.persistence.exception.feign;

import lombok.Getter;
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;

@Getter
public class CustomerFeignException extends RuntimeException {

    private final int httpStatus;
    private final ApiResult result;

    public CustomerFeignException(int httpStatus, ApiResult result) {
        super(result == null ? null : result.getDescription());
        this.httpStatus = httpStatus;
        this.result = result;
    }
}
