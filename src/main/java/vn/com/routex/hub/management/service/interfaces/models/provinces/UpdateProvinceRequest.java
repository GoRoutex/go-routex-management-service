package vn.com.routex.hub.management.service.interfaces.models.provinces;

import jakarta.validation.Valid;
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
public class UpdateProvinceRequest extends BaseRequest {

    @Valid
    @NotNull
    private UpdateProvinceRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateProvinceRequestData {
        @NotNull
        private String id;


        private String name;
        private String code;
    }
}

