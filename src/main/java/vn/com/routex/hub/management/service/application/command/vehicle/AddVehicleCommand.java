package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record AddVehicleCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String type,
        String vehiclePlate,
        String seatCapacity,
        String manufacturer
) {
}
