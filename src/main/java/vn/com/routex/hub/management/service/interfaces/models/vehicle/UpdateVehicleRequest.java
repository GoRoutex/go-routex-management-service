package vn.com.routex.hub.management.service.interfaces.models.vehicle;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateVehicleRequest extends BaseRequest {

    @Valid
    @NotNull
    private UpdateVehicleRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateVehicleRequestData {
        @NotNull
        @NotBlank
        private String creator;

        @NotNull
        @NotBlank
        private String vehicleId;

        private String type; // BUS|TRUCK|LIMOUSINE
        private String vehiclePlate;
        private String seatCapacity;
        private String manufacturer;
        private Boolean hasFloor;
        private VehicleStatus status;
    }
}

