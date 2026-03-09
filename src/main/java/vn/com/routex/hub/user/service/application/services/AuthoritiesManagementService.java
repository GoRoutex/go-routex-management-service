package vn.com.routex.hub.user.service.application.services;

import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleResponse;

public interface AuthoritiesManagementService {
    AddRoleResponse addRole(AddRoleRequest request);
    AddPermissionResponse addPermission(AddPermissionRequest request);
}
