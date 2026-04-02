package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleType;

import java.util.List;

@Builder
public record FetchVehiclesResult(
        List<FetchVehicleItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    @Builder
    public record FetchVehicleItemResult(
            String id,
            String creator,
            VehicleStatus status,
            VehicleType type,
            String vehiclePlate,
            Integer seatCapacity,
            Boolean hasFloor,
            String manufacturer
    ) {
    }
}

