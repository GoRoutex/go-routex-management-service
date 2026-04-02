package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.RoleAggregate;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthoritiesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.RolesEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Component
final class RolePersistenceMapper {

    static RoleAggregate toAggregate(RolesEntity roleJpaEntity) {
        Set<String> authorityCodes = roleJpaEntity.getAuthorities() == null ? Set.of() : roleJpaEntity.getAuthorities().stream()
                .map(AuthoritiesEntity::getCode)
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
