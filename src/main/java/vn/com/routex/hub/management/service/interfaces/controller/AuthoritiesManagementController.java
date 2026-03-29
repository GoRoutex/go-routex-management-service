package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.management.service.application.command.authorities.AddPermissionCommand;
import vn.com.routex.hub.management.service.application.command.authorities.AddPermissionResult;
import vn.com.routex.hub.management.service.application.command.authorities.AddRoleCommand;
import vn.com.routex.hub.management.service.application.command.authorities.AddRoleResult;
import vn.com.routex.hub.management.service.application.command.authorities.SetPermissionCommand;
import vn.com.routex.hub.management.service.application.command.authorities.SetPermissionResult;
import vn.com.routex.hub.management.service.application.command.authorities.SetRoleCommand;
import vn.com.routex.hub.management.service.application.command.authorities.SetRoleResult;
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
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ADD_PERMISSIONS;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ADD_ROLES;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.AUTHORITIES_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SET_PERMISSIONS;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SET_ROLE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasAuthority('authorities:management')")
public class AuthoritiesManagementController {


    private final AuthoritiesManagementService authoritiesManagementService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(AUTHORITIES_PATH + ADD_ROLES)
    public ResponseEntity<AddRoleResponse> addRole(@Valid @RequestBody AddRoleRequest request) {
        AddRoleResult result = authoritiesManagementService.addRole(AddRoleCommand.builder()
                .code(request.getData().getCode())
                .name(request.getData().getName())
                .description(request.getData().getDescription())
                .creator(request.getData().getCreator())
                .enabled(request.getData().isEnabled())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        AddRoleResponse response = AddRoleResponse.builder()
                .result(successResult())
                .data(AddRoleResponse.AddRoleResponseData.builder()
                        .code(result.getCode())
                        .name(result.getName())
                        .creator(result.getCreator())
                        .description(result.getDescription())
                        .build())
                .build();
        return HttpResponseUtil.buildResponse(request, response);
    }
    @PostMapping(AUTHORITIES_PATH + ADD_PERMISSIONS)
    public ResponseEntity<AddPermissionResponse> addPermission(@Valid @RequestBody AddPermissionRequest request) {
        AddPermissionResult result = authoritiesManagementService.addPermission(AddPermissionCommand.builder()
                .code(request.getData().getCode())
                .name(request.getData().getName())
                .description(request.getData().getDescription())
                .creator(request.getData().getCreator())
                .enabled(request.getData().isEnabled())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        AddPermissionResponse response = AddPermissionResponse.builder()
                .result(successResult())
                .data(AddPermissionResponse.AddPermissionResponseData.builder()
                        .code(result.getCode())
                        .name(result.getName())
                        .creator(result.getCreator())
                        .description(result.getDescription())
                        .build())
                .build();
        return HttpResponseUtil.buildResponse(request, response);
    }

    @PostMapping(AUTHORITIES_PATH + SET_ROLE)
    public ResponseEntity<SetRoleResponse> setRole(@Valid @RequestBody SetRoleRequest request) {
        SetRoleResult result = authoritiesManagementService.setRole(SetRoleCommand.builder()
                .userId(request.getData().getUserId())
                .roleId(request.getData().getRoleId())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        SetRoleResponse response = SetRoleResponse.builder()
                .result(successResult())
                .data(SetRoleResponse.SetRoleResponseData.builder()
                        .userId(result.getUserId())
                        .roleId(result.getRoleId())
                        .assignedAt(result.getAssignedAt())
                        .build())
                .build();
        return HttpResponseUtil.buildResponse(request, response);
    }

    @PostMapping(AUTHORITIES_PATH + SET_PERMISSIONS)
    public ResponseEntity<SetPermissionResponse> setPermission(@Valid @RequestBody SetPermissionRequest request) {
        SetPermissionResult result = authoritiesManagementService.setPermission(SetPermissionCommand.builder()
                .roleId(request.getData().getRoleId())
                .authoritiesCode(request.getData().getAuthoritiesCode())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        SetPermissionResponse response = SetPermissionResponse.builder()
                .result(successResult())
                .data(SetPermissionResponse.SetPermissionResponseData.builder()
                        .roleId(result.getRoleId())
                        .authorities(result.getAuthorities())
                        .build())
                .build();
        return HttpResponseUtil.buildResponse(request, response);
    }

    private ApiResult successResult() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }

}
