package vn.com.routex.hub.management.service.interfaces.models.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SetRoleResponse extends BaseResponse<SetRoleResponse.SetRoleResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class SetRoleResponseData {
        private String userId;
        private String roleId;
        private OffsetDateTime assignedAt;
    }
}
