package vn.com.routex.hub.management.service.interfaces.models.operationpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteOperationPointResponse extends BaseResponse<DeleteOperationPointResponse.DeleteOperationPointResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteOperationPointResponseData {
        private String id;
        private String code;
        private OperationPointStatus status;
    }
}
