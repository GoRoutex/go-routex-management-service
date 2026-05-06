package vn.com.routex.hub.management.service.application.command.department;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.domain.department.DepartmentType;

@Builder
public record UpdateDepartmentResult(
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
