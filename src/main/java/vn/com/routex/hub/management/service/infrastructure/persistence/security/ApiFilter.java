package vn.com.routex.hub.management.service.infrastructure.persistence.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import vn.com.go.routex.identity.security.jwt.JwtService;
import vn.com.routex.hub.management.service.infrastructure.persistence.config.RequestAttributes;
import vn.com.routex.hub.management.service.infrastructure.persistence.security.envelope.RequestEnvelopeExtractor;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if(shouldByPass(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean multiPartRequest = isMultipartRequest(request);
        HttpServletRequest requestToUse = request;

        if (!multiPartRequest) {
            requestToUse = new CachedHttpServletRequestWrapper(request);
        }
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
        try {
            BaseRequest baseRequest = extractBaseRequest(requestToUse, multiPartRequest);
            request.setAttribute(RequestAttributes.REQUEST_ID, baseRequest.getRequestId());
            request.setAttribute(RequestAttributes.REQUEST_DATE_TIME, baseRequest.getRequestDateTime());
            request.setAttribute(RequestAttributes.CHANNEL, baseRequest.getChannel());
            String merchantId = extractMerchantIdFromJwt(request);
            if (merchantId != null && !merchantId.isBlank()) {
                request.setAttribute(RequestAttributes.MERCHANT_ID, merchantId.trim());
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            // Invalid envelope request (requestId/requestDateTime/channel missing/invalid JSON).
            log.warn("Invalid request envelope: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid Request");
            response.getWriter().flush();
            return;
        }

        try {
            filterChain.doFilter(requestToUse, contentCachingResponseWrapper);
        } finally {
            // Always mirror the response back to the client and log it.
            String responseMessage = new String(contentCachingResponseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info("{}", responseMessage);
            contentCachingResponseWrapper.copyBodyToResponse();
        }
    }

    private boolean isNonJsonBodyRequest(HttpServletRequest request) {
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method)
                || "OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        String contentType = request.getContentType();
        if (contentType == null || contentType.isBlank()) {
            return true;
        }

        String normalizedContentType = contentType.toLowerCase();
        return !normalizedContentType.startsWith(MediaType.APPLICATION_JSON_VALUE);
    }

    private String headerAny(HttpServletRequest request, List<String> names) {
        for (String name : names) {
            String value = request.getHeader(name);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private BaseRequest extractFromHeadersOrParameters(HttpServletRequest request) {
        String requestId = firstNonBlank(
                request.getHeader(RequestAttributes.REQUEST_ID),
                headerAny(request, List.of("RT-REQUEST-ID", "X-Request-Id")),
                request.getParameter("requestId")
        );
        String requestDateTime = firstNonBlank(
                request.getHeader(RequestAttributes.REQUEST_DATE_TIME),
                headerAny(request, List.of("RT-REQUEST-DATE-TIME", "X-Request-DateTime")),
                request.getParameter("requestDateTime")
        );
        String channel = firstNonBlank(
                request.getHeader(RequestAttributes.CHANNEL),
                headerAny(request, List.of("X-Channel")),
                request.getParameter("channel")
        );

        if (requestId == null || requestDateTime == null || channel == null) {
            throw new IllegalArgumentException("Missing request envelope headers");
        }

        return BaseRequest.builder()
                .requestId(requestId)
                .requestDateTime(requestDateTime)
                .channel(channel)
                .build();
    }

    private boolean shouldByPass(String requestURI) {
        return requestURI.startsWith("/actuator/") ||
                requestURI.contains("/location-service/") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs");
    }

    private BaseRequest extractBaseRequest(HttpServletRequest request, boolean multipartRequest)
            throws IOException {
        if (multipartRequest || isNonJsonBodyRequest(request)) {
            return extractFromHeadersOrParameters(request);
        }

        String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return RequestEnvelopeExtractor.extract(request, requestBody, objectMapper);
    }

    private boolean isMultipartRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if(contentType == null) {
            return false;
        }

        return contentType.toLowerCase().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
    }

    private String extractMerchantIdFromJwt(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        try {
            String token = authorization.substring(7);
            var claims = jwtService.extractAllClaims(token);

            String merchantId = claims.get("merchantId", String.class);
            if (merchantId != null && !merchantId.isBlank()) {
                return merchantId;
            }

            Object fallback = claims.get("merchant_id");
            return fallback == null ? null : fallback.toString();
        } catch (Exception ignored) {
            return null;
        }
    }
}
