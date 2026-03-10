package vn.com.routex.hub.management.service.interfaces.models.assignment;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AssignRouteResponse extends BaseResponse<AssignRouteResponse.AssignRouteResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class AssignRouteResponseData {
        private String creator;
        private String routeId;
        private String vehicleId;
        private String assignedAt;
        private String status;
    }
}
