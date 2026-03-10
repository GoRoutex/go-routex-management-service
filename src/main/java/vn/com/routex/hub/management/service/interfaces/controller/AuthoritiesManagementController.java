package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.management.service.application.facade.AuthoritiesManagementFacade;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.AddRoleResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetPermissionRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetPermissionResponse;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetRoleRequest;
import vn.com.routex.hub.management.service.interfaces.models.authorities.SetRoleResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ADD_PERMISSIONS;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ADD_ROLES;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.AUTHORITIES_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SET_PERMISSIONS;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SET_ROLES;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
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

    @PostMapping(AUTHORITIES_PATH + SET_ROLES)
    public ResponseEntity<SetRoleResponse> setRole(@Valid @RequestBody SetRoleRequest request) {
        return authoritiesManagementFacade.setRole(request);
    }

    @PostMapping(AUTHORITIES_PATH + SET_PERMISSIONS)
    public ResponseEntity<SetPermissionResponse> setPermission(@Valid @RequestBody SetPermissionRequest request) {
        return  authoritiesManagementFacade.setPermission(request);
    }

}
