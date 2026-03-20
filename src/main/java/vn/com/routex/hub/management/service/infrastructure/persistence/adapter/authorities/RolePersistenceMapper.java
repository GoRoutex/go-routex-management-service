package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import vn.com.routex.hub.management.service.domain.authorities.model.RoleAggregate;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthorityJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.RoleJpaEntity;

import java.util.Set;
import java.util.stream.Collectors;

final class RolePersistenceMapper {

    private RolePersistenceMapper() {
    }

    static RoleAggregate toAggregate(RoleJpaEntity roleJpaEntity) {
        Set<String> authorityCodes = roleJpaEntity.getAuthorities() == null ? Set.of() : roleJpaEntity.getAuthorities().stream()
                .map(AuthorityJpaEntity::getCode)
                .collect(Collectors.toSet());

        return RoleAggregate.builder()
                .id(roleJpaEntity.getId())
                .code(roleJpaEntity.getCode())
                .name(roleJpaEntity.getName())
                .description(roleJpaEntity.getDescription())
                .enabled(roleJpaEntity.getEnabled())
                .createdAt(roleJpaEntity.getCreatedAt())
                .createdBy(roleJpaEntity.getCreatedBy())
                .authorityCodes(authorityCodes)
                .build();
    }
}
