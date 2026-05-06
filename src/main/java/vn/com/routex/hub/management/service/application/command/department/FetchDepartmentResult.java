package vn.com.routex.hub.management.service.application.command.department;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.domain.department.DepartmentType;

import java.util.List;

@Builder
public record FetchDepartmentResult(
        List<FetchDepartmentItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    @Builder
    public record FetchDepartmentItemResult(
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
}

