package vn.com.routex.hub.management.service.application.command.department;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;

@Builder
public record DeleteDepartmentResult(
        String id,
        String code,
        DepartmentStatus status
) {
}
