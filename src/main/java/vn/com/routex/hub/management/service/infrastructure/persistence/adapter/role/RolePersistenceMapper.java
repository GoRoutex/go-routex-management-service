package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.role;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.RoleAggregate;
import vn.com.routex.hub.management.service.domain.role.model.Authorities;
import vn.com.routex.hub.management.service.domain.role.model.UserRoleId;
import vn.com.routex.hub.management.service.domain.role.model.UserRoles;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthoritiesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.RolesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.UserRoleEntityId;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.UserRolesEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RolePersistenceMapper {

    public RoleAggregate toDomain(RolesEntity rolesJpaEntity) {
        Set<Authorities> authorities = rolesJpaEntity.getAuthorities().stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());

        return RoleAggregate.builder()
                .id(rolesJpaEntity.getId())
                .code(rolesJpaEntity.getCode())
                .name(rolesJpaEntity.getName())
                .description(rolesJpaEntity.getDescription())
                .enabled(rolesJpaEntity.getEnabled())
                .authorityCodes(authorities.stream().map(Authorities::getCode).collect(Collectors.toSet()))
                .createdAt(rolesJpaEntity.getCreatedAt())
                .createdBy(rolesJpaEntity.getCreatedBy())
                .updatedAt(rolesJpaEntity.getUpdatedAt())
                .updatedBy(rolesJpaEntity.getUpdatedBy())
                .build();
    }

    public Authorities toDomain(AuthoritiesEntity authoritiesJpaEntity) {
        return Authorities.builder()
                .id(authoritiesJpaEntity.getId())
                .code(authoritiesJpaEntity.getCode())
                .name(authoritiesJpaEntity.getName())
                .description(authoritiesJpaEntity.getDescription())
                .enabled(authoritiesJpaEntity.getEnabled())
                .createdAt(authoritiesJpaEntity.getCreatedAt())
                .createdBy(authoritiesJpaEntity.getCreatedBy())
                .updatedAt(authoritiesJpaEntity.getUpdatedAt())
                .updatedBy(authoritiesJpaEntity.getUpdatedBy())
                .build();
    }

    public UserRoles toDomain(UserRolesEntity userRolesJpaEntity) {
        return UserRoles.builder()
                .id(UserRoleId.builder()
                        .userId(userRolesJpaEntity.getId().getUserId())
                        .roleId(userRolesJpaEntity.getId().getRoleId())
                        .build())
                .assignedAt(userRolesJpaEntity.getAssignedAt())
                .build();
    }

    public UserRolesEntity toJpaEntity(UserRoles userRoles) {
        return UserRolesEntity.builder()
                .id(UserRoleEntityId.builder()
                        .userId(userRoles.getId().getUserId())
                        .roleId(userRoles.getId().getRoleId())
                        .build())
                .assignedAt(userRoles.getAssignedAt())
                .build();
    }
}
