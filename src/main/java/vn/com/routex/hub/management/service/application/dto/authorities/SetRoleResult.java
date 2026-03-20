package vn.com.routex.hub.management.service.application.dto.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SetRoleResult {
    private String userId;
    private String roleId;
    private OffsetDateTime assignedAt;
}
