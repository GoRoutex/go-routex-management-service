package vn.com.routex.hub.user.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.services.AuthoritiesManagementService;
import vn.com.routex.hub.user.service.domain.roles.Authorities;
import vn.com.routex.hub.user.service.domain.roles.AuthoritiesRepository;
import vn.com.routex.hub.user.service.domain.roles.Roles;
import vn.com.routex.hub.user.service.domain.roles.RolesRepository;
import vn.com.routex.hub.user.service.domain.roles.UserRoleId;
import vn.com.routex.hub.user.service.domain.roles.UserRoles;
import vn.com.routex.hub.user.service.domain.roles.UserRolesRepository;
import vn.com.routex.hub.user.service.domain.user.User;
import vn.com.routex.hub.user.service.domain.user.UserRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.AddRoleResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetPermissionRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetPermissionResponse;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetRoleRequest;
import vn.com.routex.hub.user.service.interfaces.models.authorities.SetRoleResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.AUTHORITIES_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_USER_ROLE_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.PERMISSION_EXISTS_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.ROLE_EXISTS_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.ROLE_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthoritiesManagementServiceImpl implements AuthoritiesManagementService {

    private final RolesRepository rolesRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;

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
                .description(request.getData().getDescription())
                .enabled(request.getData().isEnabled())
                .createdAt(OffsetDateTime.now())
                .createdBy(request.getData().getCreator())
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
                        .description(request.getData().getDescription())
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
                .description(request.getData().getDescription())
                .enabled(request.getData().isEnabled())
                .createdAt(OffsetDateTime.now())
                .createdBy(request.getData().getCreator())
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
                        .description(request.getData().getDescription())
                        .build())
                .build();
    }

    @Override
    public SetPermissionResponse setPermission(SetPermissionRequest request) {
        Roles roles = rolesRepository.findById(request.getData().getRoleId())
                .orElseThrow(() -> new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROLE_NOT_FOUND)));

        List<Authorities> authoritiesList = authoritiesRepository.findByCodeIn(request.getData().getAuthoritiesCode());

        if(authoritiesList.size() != request.getData().getAuthoritiesCode().size()) {
            Set<String> foundCodes = authoritiesList.stream()
                    .map(Authorities::getCode)
                    .collect(Collectors.toSet());

            Set<String> missingCodes = new HashSet<>(request.getData().getAuthoritiesCode());
            missingCodes.removeAll(foundCodes);

            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, AUTHORITIES_NOT_FOUND));
        }

        roles.getAuthorities().addAll(authoritiesList);
        rolesRepository.save(roles);


        return SetPermissionResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(SetPermissionResponse.SetPermissionResponseData.builder()
                        .roleId(request.getData().getRoleId())
                        .authorities(request.getData().getAuthoritiesCode())
                        .build())
                .build();
    }

    @Override
    public SetRoleResponse setRole(SetRoleRequest request) {
        Roles roles = rolesRepository.findById(request.getData().getRoleId())
                .orElseThrow(() -> new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROLE_NOT_FOUND)));

        User user = userRepository.findById(request.getData().getUserId())
                .orElseThrow(() -> new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE)));


        UserRoleId id = UserRoleId.builder()
                .roleId(request.getData().getRoleId())
                .userId(request.getData().getUserId())
                .build();

        if(userRolesRepository.existsById(id)) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, DUPLICATE_USER_ROLE_MESSAGE));
        }

        UserRoles userRoles = UserRoles.builder()
                .id(UserRoleId.builder()
                        .userId(request.getData().getUserId())
                        .roleId(request.getData().getRoleId())
                        .build())
                .assignedAt(OffsetDateTime.now())
                .build();


        userRolesRepository.save(userRoles);

        return SetRoleResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(SetRoleResponse.SetRoleResponseData
                        .builder()
                        .userId(request.getData().getUserId())
                        .roleId(request.getData().getRoleId())
                        .assignedAt(OffsetDateTime.now())
                        .build())
                .build();

    }
}
