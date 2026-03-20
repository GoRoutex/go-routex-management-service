package vn.com.routex.hub.management.service.application.dto.vehicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleType;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AddVehicleResult {
    private String creator;
    private VehicleType type;
    private String vehiclePlate;
    private String seatCapacity;
    private String manufacturer;
    private VehicleStatus status;
}
