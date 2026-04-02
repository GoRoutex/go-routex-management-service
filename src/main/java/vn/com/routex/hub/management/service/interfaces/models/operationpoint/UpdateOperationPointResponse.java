package vn.com.routex.hub.management.service.interfaces.models.operationpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointStatus;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointType;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateOperationPointResponse extends BaseResponse<UpdateOperationPointResponse.UpdateOperationPointResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateOperationPointResponseData {
        private String id;
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
