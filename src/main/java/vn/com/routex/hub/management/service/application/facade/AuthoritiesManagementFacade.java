package vn.com.routex.hub.management.service.application.facade;

import org.springframework.http.ResponseEntity;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddRoleResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetPermissionRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetPermissionResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetRoleRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetRoleResponse;

public interface AuthoritiesManagementFacade {
    ResponseEntity<AddRoleResponse> addRole(AddRoleRequest request);

    ResponseEntity<AddPermissionResponse> addPermission(AddPermissionRequest request);

    ResponseEntity<SetRoleResponse> setRole(SetRoleRequest request);

    ResponseEntity<SetPermissionResponse> setPermission(SetPermissionRequest request);
}
