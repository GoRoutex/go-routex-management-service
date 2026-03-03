package vn.com.routex.hub.user.service.interfaces.models.vehicle;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.ONLY_CHARACTER_REGEX;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.ONLY_NUMBER_REGEX;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.VEHICLE_PLATE_REGEX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AddVehicleRequest extends BaseRequest {

    @Valid
    @NotNull
    private AddVehicleRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AddVehicleRequestData {


        @NotNull
        @NotBlank
        private String creator;

        @NotNull
        @NotBlank
        @Pattern(regexp = "(BUS|TRUCK|LIMOUSINE)", message = "must be BUS, TRUCK or LIMOUSINE")
        private String type;

        @NotNull
        @NotBlank
        @Pattern(regexp = VEHICLE_PLATE_REGEX, message = "must be in format of Vietnamese Plate e.g: 51F1-268.99")
        private String vehiclePlate;

        @NotNull
        @NotBlank
        @Pattern(regexp = ONLY_NUMBER_REGEX, message = "only digits are allowed for this field")
        private String seatCapacity;

        @NotNull
        @NotBlank
        @Pattern(regexp = ONLY_CHARACTER_REGEX, message = "only characters are allowed for this field")
        private String manufacturer;
    }
}
