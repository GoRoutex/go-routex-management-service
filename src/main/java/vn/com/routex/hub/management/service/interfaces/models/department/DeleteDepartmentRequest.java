package vn.com.routex.hub.management.service.interfaces.models.department;

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
public class DeleteDepartmentRequest extends BaseRequest {

    @Valid
    @NotNull
    private DeleteDepartmentRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteDepartmentRequestData {
        @NotNull
        @NotBlank
        private String id;
    }
}

