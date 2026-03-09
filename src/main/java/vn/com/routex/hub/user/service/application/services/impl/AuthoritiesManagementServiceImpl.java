package vn.com.routex.hub.user.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.services.AuthoritiesManagementService;
import vn.com.routex.hub.user.service.domain.roles.Authorities;
import vn.com.routex.hub.user.service.domain.roles.AuthoritiesRepository;
import vn.com.routex.hub.user.service.domain.roles.Roles;
import vn.com.routex.hub.user.service.domain.roles.RolesRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import java.util.UUID;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.PERMISSION_EXISTS_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.ROLE_EXISTS_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthoritiesManagementServiceImpl implements AuthoritiesManagementService {

    private final RolesRepository rolesRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Override
    public AddRoleResponse addRole(AddRoleRequest request) {
        if (rolesRepository.existsByCode(request.getData().getCode())) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(ROLE_EXISTS_ERROR, request.getData().getCode())));
        }

        Roles roles = Roles.builder()
                .id(UUID.randomUUID().toString())
                .code(request.getData().getCode())
                .name(request.getData().getName())
                .enabled(request.getData().isEnabled())
                .build();

        rolesRepository.save(roles);

        return AddRoleResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(AddRoleResponse.AddRoleResponseData.builder()
                        .code(request.getData().getCode())
                        .name(request.getData().getName())
                        .build())
                .build();
    }

    @Override
    public AddPermissionResponse addPermission(AddPermissionRequest request) {

        if (authoritiesRepository.existsByCode(request.getData().getCode())) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(PERMISSION_EXISTS_ERROR, request.getData().getCode())));
        }

        Authorities authorities = Authorities.builder()
                .id(UUID.randomUUID().toString())
                .code(request.getData().getCode())
                .name(request.getData().getName())
                .enabled(request.getData().isEnabled())
                .build();

        authoritiesRepository.save(authorities);

        return AddPermissionResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(AddPermissionResponse.AddPermissionResponseData.builder()
                        .code(request.getData().getCode())
                        .name(request.getData().getName())
                        .build())
                .build();
    }
}
