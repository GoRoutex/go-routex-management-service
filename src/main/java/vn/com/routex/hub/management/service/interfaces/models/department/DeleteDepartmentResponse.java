package vn.com.routex.hub.management.service.interfaces.models.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteDepartmentResponse extends BaseResponse<DeleteDepartmentResponse.DeleteDepartmentResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteDepartmentResponseData {
        private String id;
        private String code;
        private DepartmentStatus status;
    }
}
