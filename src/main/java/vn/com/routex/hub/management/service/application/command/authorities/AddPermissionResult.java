package vn.com.routex.hub.management.service.application.command.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AddPermissionResult {
    private String code;
    private String name;
    private String creator;
    private String description;
}
