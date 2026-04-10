package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;

@Builder
public record UpdateVehicleCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String vehicleId,
        String type,
        String vehiclePlate,
        String seatCapacity,
        String manufacturer,
        Boolean hasFloor,
        VehicleStatus status
) {
}
