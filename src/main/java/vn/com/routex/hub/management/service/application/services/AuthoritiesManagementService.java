package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.authorities.AddPermissionCommand;
import vn.com.routex.hub.management.service.application.command.authorities.AddPermissionResult;
import vn.com.routex.hub.management.service.application.command.authorities.AddRoleCommand;
import vn.com.routex.hub.management.service.application.command.authorities.AddRoleResult;
import vn.com.routex.hub.management.service.application.command.authorities.SetPermissionCommand;
import vn.com.routex.hub.management.service.application.command.authorities.SetPermissionResult;
import vn.com.routex.hub.management.service.application.command.authorities.SetRoleCommand;
import vn.com.routex.hub.management.service.application.command.authorities.SetRoleResult;

public interface AuthoritiesManagementService {
    AddRoleResult addRole(AddRoleCommand command);

    AddPermissionResult addPermission(AddPermissionCommand command);

    SetPermissionResult setPermission(SetPermissionCommand command);

    SetRoleResult setRole(SetRoleCommand command);
}
