package vn.com.routex.hub.management.service.application.command.department;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.domain.department.DepartmentType;

@Builder
public record UpdateDepartmentCommand(
        RequestContext context,
        String merchantId,
        String id,
        String code,
        String name,
        DepartmentType type,
        String address,
        String city,
        Double latitude,
        Double longitude,
        DepartmentStatus status
) {
}
