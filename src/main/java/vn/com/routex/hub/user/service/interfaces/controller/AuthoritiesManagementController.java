package vn.com.routex.hub.user.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.user.service.application.facade.AuthoritiesManagementFacade;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.ADD_PERMISSIONS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.ADD_ROLES;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.AUTHORITIES_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
public class AuthoritiesManagementController {


    private final AuthoritiesManagementFacade authoritiesManagementFacade;

    @PostMapping(AUTHORITIES_PATH + ADD_ROLES)
    public ResponseEntity<AddRoleResponse> addRole(@Valid @RequestBody AddRoleRequest request) {
        return authoritiesManagementFacade.addRole(request);
    }
    @PostMapping(AUTHORITIES_PATH + ADD_PERMISSIONS)
    public ResponseEntity<AddPermissionResponse> addPermission(@Valid @RequestBody AddPermissionRequest request) {
        return authoritiesManagementFacade.addPermission(request);
    }

}
