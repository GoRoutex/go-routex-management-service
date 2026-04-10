package vn.com.routex.hub.management.service.application.command.provinces;

import lombok.Builder;

import java.util.List;

@Builder
public record FetchProvincesResult(
        List<FetchProvinceResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    @Builder
    public record FetchProvinceResult(
            int id,
            String code,
            String name
    ) {
    }
}
