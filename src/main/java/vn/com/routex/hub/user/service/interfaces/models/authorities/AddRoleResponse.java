package vn.com.routex.hub.user.service.interfaces.models.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AddRoleResponse extends BaseResponse<AddRoleResponse.AddRoleResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class AddRoleResponseData {
        private String code;
        private String name;
    }
}
