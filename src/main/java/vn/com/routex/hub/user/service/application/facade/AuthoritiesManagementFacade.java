package vn.com.routex.hub.user.service.application.facade;

import org.springframework.http.ResponseEntity;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleResponse;

public interface AuthoritiesManagementFacade {
    ResponseEntity<AddRoleResponse> addRole(AddRoleRequest request);

    ResponseEntity<AddPermissionResponse> addPermission(AddPermissionRequest request);
}
