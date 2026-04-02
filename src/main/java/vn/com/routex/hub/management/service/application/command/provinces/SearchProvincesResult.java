package vn.com.routex.hub.management.service.application.command.provinces;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchProvincesResult(
        List<SearchProvincesItemResult> data
) {
    @Builder
    public record SearchProvincesItemResult(
            int id,
            String name,
            String code
    ) {
    }
}
