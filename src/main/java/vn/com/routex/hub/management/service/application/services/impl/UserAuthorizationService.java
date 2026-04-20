package vn.com.routex.hub.management.service.application.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.domain.authorities.model.RoleAggregate;
import vn.com.routex.hub.management.service.domain.merchant.MerchantUserStatus;
import vn.com.routex.hub.management.service.domain.role.port.RoleRepositoryPort;
import vn.com.routex.hub.management.service.domain.role.port.UserRoleRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.repository.MerchantUserEntityRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.AUTHORIZATION_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROLE_NOT_FOUND_ERROR;


@RequiredArgsConstructor
@Service
public class UserAuthorizationService {

    private final UserRoleRepositoryPort userRoleRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final MerchantUserEntityRepository merchantUserJpaRepository;

    public Set<String> getRoles(String userId) {
        return getUserRoles(userId).stream()
                .map(role -> "ROLE_" + role.getCode())
                .collect(Collectors.toSet());
    }

    public Set<String> getAuthorities(String userId) {
        return getUserRoles(userId).stream()
                .flatMap(role -> role.getAuthorityCodes().stream())
                .collect(Collectors.toSet());
    }

    public Optional<String> getMerchantId(String userId) {
        return merchantUserJpaRepository.findByUserIdAndStatus(userId, MerchantUserStatus.ACTIVE)
                .stream()
                .map(merchantUser -> merchantUser != null ? merchantUser.getMerchantId() : null)
                .filter(Objects::nonNull)
                .filter(merchantId -> !merchantId.isBlank())
                .findFirst();
    }

    private Set<RoleAggregate> getUserRoles(String userId) {
        Stream<RoleAggregate> defaultRoles = userRoleRepositoryPort.findByUserId(userId)
                .stream()
                .map(userRole -> roleRepositoryPort.findById(userRole.getId().getRoleId())
                        .orElseThrow(() -> missingRoleById(userRole.getId().getRoleId())));

        Stream<RoleAggregate> merchantRoles = merchantUserJpaRepository.findByUserIdAndStatus(userId, MerchantUserStatus.ACTIVE)
                .stream()
                .map(merchantUser -> roleRepositoryPort.findByCode(merchantUser.getRoleCode())
                        .orElseThrow(() -> missingRoleByCode(merchantUser.getRoleCode())));

        return Stream.concat(defaultRoles, merchantRoles)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private BusinessException missingRoleById(String roleId) {
        return new BusinessException(ExceptionUtils.buildResultResponse(
                AUTHORIZATION_ERROR,
                String.format(ROLE_NOT_FOUND_ERROR, roleId)
        ));
    }

    private BusinessException missingRoleByCode(String roleCode) {
        return new BusinessException(ExceptionUtils.buildResultResponse(
                AUTHORIZATION_ERROR,
                String.format(ROLE_NOT_FOUND_ERROR, roleCode)
        ));
    }
}
