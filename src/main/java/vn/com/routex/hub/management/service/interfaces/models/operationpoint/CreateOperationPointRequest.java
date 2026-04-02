package vn.com.routex.hub.management.service.interfaces.models.operationpoint;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointStatus;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointType;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateOperationPointRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateOperationPointRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateOperationPointRequestData {
        private String code;
        private String name;
        private OperationPointType type;
        private String address;
        private String city;
        private Double latitude;
        private Double longitude;
        private OperationPointStatus status;
    }


}
