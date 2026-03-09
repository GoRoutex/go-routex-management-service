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
public class AddPermissionResponse extends BaseResponse<AddPermissionResponse.AddPermissionResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class AddPermissionResponseData {
        private String code;
        private String name;
        private String creator;
        private String description;

    }
}
