package vn.com.routex.hub.management.service.interfaces.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.user.model.UserStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteUserResponse extends BaseResponse<DeleteUserResponse.DeleteUserResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteUserResponseData {
        private String id;
        private UserStatus status;
    }
}
