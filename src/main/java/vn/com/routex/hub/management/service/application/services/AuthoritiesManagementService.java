package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.dto.authorities.AddPermissionCommand;
import vn.com.routex.hub.management.service.application.dto.authorities.AddPermissionResult;
import vn.com.routex.hub.management.service.application.dto.authorities.AddRoleCommand;
import vn.com.routex.hub.management.service.application.dto.authorities.AddRoleResult;
import vn.com.routex.hub.management.service.application.dto.authorities.SetPermissionCommand;
import vn.com.routex.hub.management.service.application.dto.authorities.SetPermissionResult;
import vn.com.routex.hub.management.service.application.dto.authorities.SetRoleCommand;
import vn.com.routex.hub.management.service.application.dto.authorities.SetRoleResult;

public interface AuthoritiesManagementService {
    AddRoleResult addRole(AddRoleCommand command);

    AddPermissionResult addPermission(AddPermissionCommand command);

    SetPermissionResult setPermission(SetPermissionCommand command);

    SetRoleResult setRole(SetRoleCommand command);
}
