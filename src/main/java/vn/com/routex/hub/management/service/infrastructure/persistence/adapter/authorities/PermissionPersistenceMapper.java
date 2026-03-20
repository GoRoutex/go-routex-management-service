package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import vn.com.routex.hub.management.service.domain.authorities.model.PermissionProfile;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthorityJpaEntity;

final class PermissionPersistenceMapper {

    private PermissionPersistenceMapper() {
    }

    static PermissionProfile toProfile(AuthorityJpaEntity authorityJpaEntity) {
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

    static AuthorityJpaEntity toEntity(PermissionProfile permissionProfile) {
        return AuthorityJpaEntity.builder()
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
