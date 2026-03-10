package vn.com.routex.hub.management.service.interfaces.models.assignment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AssignRouteRequest extends BaseRequest {


    @Valid
    @NotNull
    private AssignRouteRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AssignRouteRequestData {

        @NotNull
        @NotBlank
        private String creator;

        @NotNull
        @NotBlank
        private String routeId;

        @NotNull
        @NotBlank
        private String vehicleId;
    }
}
