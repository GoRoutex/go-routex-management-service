package vn.com.routex.hub.management.service.application.dto.authorities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SetRoleCommand {
    private String userId;
    private String roleId;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
