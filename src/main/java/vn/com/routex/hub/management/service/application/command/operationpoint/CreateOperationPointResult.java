package vn.com.routex.hub.management.service.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointStatus;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointType;

@Builder
public record CreateOperationPointResult(
        String id,
        String code,
        String name,
        OperationPointType type,
        String address,
        String city,
        Double latitude,
        Double longitude,
        OperationPointStatus status
) {
}
