package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;

@Builder
public record DeleteVehicleCommand(
        String creator,
        String vehicleId,
        String requestId,
        String requestDateTime,
        String channel
) {
}

