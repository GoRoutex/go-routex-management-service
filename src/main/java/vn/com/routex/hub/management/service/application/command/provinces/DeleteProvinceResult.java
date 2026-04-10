package vn.com.routex.hub.management.service.application.command.provinces;

import lombok.Builder;

@Builder
public record DeleteProvinceResult(
        int id
) {
}

