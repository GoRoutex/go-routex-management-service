package vn.com.routex.hub.management.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.services.AuthoritiesManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpResponseUtil;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddRoleResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetPermissionRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetPermissionResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetRoleRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetRoleResponse;

@Component
@RequiredArgsConstructor
public class AuthoritiesManagementFacadeImpl implements AuthoritiesManagementFacade {

    private final AuthoritiesManagementService authoritiesManagementService;

    @Override
    public ResponseEntity<AddRoleResponse> addRole(AddRoleRequest request) {
        AddRoleResponse response = authoritiesManagementService.addRole(request);
        return HttpResponseUtil.buildResponse(request, response);
    }

    @Override
    public ResponseEntity<AddPermissionResponse> addPermission(AddPermissionRequest request) {
        AddPermissionResponse response = authoritiesManagementService.addPermission(request);
        return HttpResponseUtil.buildResponse(request, response);
    }

    @Override
    public ResponseEntity<SetRoleResponse> setRole(SetRoleRequest request) {
        SetRoleResponse response = authoritiesManagementService.setRole(request);
        return HttpResponseUtil.buildResponse(request, response);
    }

    @Override
    public ResponseEntity<SetPermissionResponse> setPermission(SetPermissionRequest request) {
        SetPermissionResponse response = authoritiesManagementService.setPermission(request);
        return HttpResponseUtil.buildResponse(request, response);
    }
}
