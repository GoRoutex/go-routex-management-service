package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.PermissionProfile;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthoritiesEntity;


@Component
@NoArgsConstructor
final class PermissionPersistenceMapper {

    static PermissionProfile toProfile(AuthoritiesEntity authorityJpaEntity) {
        return PermissionProfile.builder()
                .id(authorityJpaEntity.getId())
                .code(authorityJpaEntity.getCode())
                .name(authorityJpaEntity.getName())
                .description(authorityJpaEntity.getDescription())
                .enabled(authorityJpaEntity.getEnabled())
                .createdAt(authorityJpaEntity.getCreatedAt())
                .createdBy(authorityJpaEntity.getCreatedBy())
                .build();
    }

    static AuthoritiesEntity toEntity(PermissionProfile permissionProfile) {
        return AuthoritiesEntity.builder()
                .id(permissionProfile.getId())
                .code(permissionProfile.getCode())
                .name(permissionProfile.getName())
                .description(permissionProfile.getDescription())
                .enabled(permissionProfile.getEnabled())
                .createdAt(permissionProfile.getCreatedAt())
                .createdBy(permissionProfile.getCreatedBy())
                .build();
    }
}
