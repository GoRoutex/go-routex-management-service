package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;

@Builder
public record DeleteVehicleResult(
        String id,
        VehicleStatus status
) {
}

