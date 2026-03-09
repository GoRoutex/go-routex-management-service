package vn.com.routex.hub.user.service.interfaces.models.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SetPermissionResponse extends BaseResponse<SetPermissionResponse.SetPermissionResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class SetPermissionResponseData {
        private String roleId;
        private Set<String> authorities;
    }
}
