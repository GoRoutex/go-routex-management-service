package vn.com.routex.hub.management.service.interfaces.models.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.user.model.Gender;
import vn.com.routex.hub.management.service.domain.user.model.UserStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateUserRequest extends BaseRequest {

    @Valid
    @NotNull
    private UpdateUserRequestData data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class UpdateUserRequestData {
        @NotBlank
        private String userId;
        private String email;
        private String phoneNumber;
        private String avatarUrl;
        private String address;
        private LocalDate dob;
        private Gender gender;
        private String nationalId;
        private Boolean phoneVerified;
        private Boolean profileCompleted;
        private Boolean emailVerified;
        private UserStatus status;
        private String language;
        private String timezone;
        private Integer failLoginCount;
        private OffsetDateTime lastLoginAt;
        private OffsetDateTime lockedUntil;
        private String updatedBy;
    }
}
