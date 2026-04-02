package vn.com.routex.hub.management.service.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointStatus;

@Builder
public record DeleteOperationPointResult(
        String id,
        String code,
        OperationPointStatus status
) {
}
