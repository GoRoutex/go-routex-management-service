package vn.com.routex.hub.user.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.application.services.AuthoritiesManagementService;
import vn.com.routex.hub.user.service.infrastructure.utils.HttpResponseUtil;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleResponse;

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
}
