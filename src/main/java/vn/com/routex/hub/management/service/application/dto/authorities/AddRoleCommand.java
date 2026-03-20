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
public class AddRoleCommand {
    private String code;
    private String name;
    private String description;
    private String creator;
    private boolean enabled;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
