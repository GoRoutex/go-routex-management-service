package vn.com.routex.hub.management.service.interfaces.models.vehicle;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleType;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AddVehicleResponse extends BaseResponse<AddVehicleResponse.AddVehicleResponseData> {
    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class AddVehicleResponseData {
        private String creator;
        private VehicleType type;
        private String vehiclePlate;
        private String seatCapacity;
        private String manufacturer;
        private VehicleStatus status;
    }
}
