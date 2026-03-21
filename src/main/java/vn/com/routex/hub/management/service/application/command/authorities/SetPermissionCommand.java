package vn.com.routex.hub.management.service.application.command.authorities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SetPermissionCommand {
    private String roleId;
    private Set<String> authoritiesCode;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
