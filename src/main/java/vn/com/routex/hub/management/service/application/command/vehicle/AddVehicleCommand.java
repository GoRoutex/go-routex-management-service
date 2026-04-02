package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.Builder;

@Builder
public record AddVehicleCommand(
        String creator,
        String type,
        String vehiclePlate,
        String seatCapacity,
        String manufacturer,
        String requestId,
        String requestDateTime,
        String channel
) {
}
