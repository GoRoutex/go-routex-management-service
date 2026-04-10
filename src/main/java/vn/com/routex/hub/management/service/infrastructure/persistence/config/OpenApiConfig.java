package vn.com.routex.hub.management.service.infrastructure.persistence.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI managementServiceOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer token")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME));
    }

    @Bean
    public OperationCustomizer envelopeHeadersForGetCustomizer() {
        return (operation, handlerMethod) -> {
            ensureBearerAuth(operation);

            if (!isGetOperation(operation, handlerMethod)) {
                return operation;
            }

            ensureHeader(operation, RequestAttributes.REQUEST_ID,
                    "Envelope request id. Example: 123e4567-e89b-12d3-a456-426614174000");
            ensureHeader(operation, RequestAttributes.REQUEST_DATE_TIME,
                    "Envelope request datetime. Example: 2026-04-10T20:00:00.000+07:00");
            ensureHeader(operation, RequestAttributes.CHANNEL,
                    "Envelope channel. Allowed values: ONL, OFF");

            return operation;
        };
    }

    private void ensureBearerAuth(Operation operation) {
        List<SecurityRequirement> securityRequirements = operation.getSecurity();
        if (securityRequirements == null) {
            securityRequirements = new ArrayList<>();
            operation.setSecurity(securityRequirements);
        }

        boolean exists = securityRequirements.stream()
                .anyMatch(requirement -> requirement.containsKey(BEARER_AUTH_SCHEME));
        if (!exists) {
            securityRequirements.add(new SecurityRequirement().addList(BEARER_AUTH_SCHEME));
        }
    }

    private boolean isGetOperation(Operation operation, HandlerMethod handlerMethod) {
        if (handlerMethod == null) {
            return false;
        }

        if (handlerMethod.hasMethodAnnotation(org.springframework.web.bind.annotation.GetMapping.class)) {
            return true;
        }

        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(
                handlerMethod.getMethod(),
                RequestMapping.class
        );

        if (requestMapping == null) {
            return false;
        }

        for (org.springframework.web.bind.annotation.RequestMethod requestMethod : requestMapping.method()) {
            if (HttpMethod.GET.matches(requestMethod.name())) {
                return true;
            }
        }

        return false;
    }

    private void ensureHeader(Operation operation, String headerName, String description) {
        List<Parameter> parameters = operation.getParameters();
        if (parameters == null) {
            parameters = new ArrayList<>();
            operation.setParameters(parameters);
        }

        boolean exists = parameters.stream()
                .anyMatch(parameter -> "header".equalsIgnoreCase(parameter.getIn())
                        && headerName.equalsIgnoreCase(parameter.getName()));
        if (exists) {
            return;
        }

        parameters.add(new Parameter()
                .in("header")
                .required(true)
                .name(headerName)
                .description(description));
    }
}
