package vn.com.routex.hub.management.service.application.command.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AddVehicleCommand {
    private String creator;
    private String type;
    private String vehiclePlate;
    private String seatCapacity;
    private String manufacturer;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
