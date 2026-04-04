package vn.com.routex.hub.management.service.infrastructure.persistence.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import vn.com.routex.hub.management.service.infrastructure.persistence.config.RequestAttributes;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

@UtilityClass
public class ApiRequestUtils {
    public static BaseRequest getBaseRequestOrDefault(HttpServletRequest request) {

        String requestId =
                (String) request.getAttribute(RequestAttributes.REQUEST_ID);

        String requestDateTime =
                (String) request.getAttribute(RequestAttributes.REQUEST_DATE_TIME);

        String requestChannel =
                (String) request.getAttribute(RequestAttributes.CHANNEL);

        return BaseRequest.builder()
                .requestId(requestId)
                .requestDateTime(requestDateTime)
                .channel(requestChannel)
                .build();
    }

    public static int parseIntOrDefault(
            String v,
            int defaultValue,
            String field,
            String requestId,
            String requestDateTime,
            String channel
    ) {
        if (v == null || v.isBlank()) return defaultValue;
        return DateTimeUtils.parseIntOrThrow(v, field, requestId, requestDateTime, channel);
    }


    public static String firstNonBlank(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }
}
