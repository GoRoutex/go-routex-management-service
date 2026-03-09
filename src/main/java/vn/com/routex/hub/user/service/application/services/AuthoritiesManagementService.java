package vn.com.routex.hub.user.service.application.services;

import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetRoleResponse;

public interface AuthoritiesManagementService {
    AddRoleResponse addRole(AddRoleRequest request);
    AddPermissionResponse addPermission(AddPermissionRequest request);
    SetPermissionResponse setPermission(SetPermissionRequest request);
    SetRoleResponse setRole(SetRoleRequest request);
}
