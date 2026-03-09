package vn.com.routex.hub.user.service.interfaces.models.authorities;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AddRoleRequest extends BaseRequest {

    @Valid
    @NotNull
    private AddRoleRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AddRoleRequestData {

        @NotBlank
        @NotNull
        private String code;

        @NotBlank
        @NotNull
        private String name;
        private boolean enabled;
    }
}
