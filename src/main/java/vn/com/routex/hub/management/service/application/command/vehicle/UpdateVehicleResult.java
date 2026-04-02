package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleType;

@Builder
public record UpdateVehicleResult(
        String id,
        String creator,
        VehicleType type,
        String vehiclePlate,
        Integer seatCapacity,
        Boolean hasFloor,
        String manufacturer,
        VehicleStatus status
) {
}

