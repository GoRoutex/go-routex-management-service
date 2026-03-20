package vn.com.routex.hub.management.service.application.dto.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SetPermissionResult {
    private String roleId;
    private Set<String> authorities;
}
