package vn.com.routex.hub.management.service.interfaces.models.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.domain.department.DepartmentType;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateDepartmentResponse extends BaseResponse<CreateDepartmentResponse.CreateDepartmentResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateDepartmentResponseData {
        private String id;
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
