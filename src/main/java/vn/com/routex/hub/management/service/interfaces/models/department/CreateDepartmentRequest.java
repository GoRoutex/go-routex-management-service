package vn.com.routex.hub.management.service.interfaces.models.department;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.domain.department.DepartmentType;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateDepartmentRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateDepartmentRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateDepartmentRequestData {
        private String code;
        private String name;
        private DepartmentType type;
        private String address;
        private String city;
        private Double latitude;
        private Double longitude;
        private DepartmentStatus status;
    }


}
