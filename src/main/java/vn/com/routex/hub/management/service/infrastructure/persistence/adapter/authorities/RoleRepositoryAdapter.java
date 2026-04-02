package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.RoleAggregate;
import vn.com.routex.hub.management.service.domain.authorities.port.RoleRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthoritiesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.RolesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository.AuthoritiesEntityRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository.RolesEntityRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RolesEntityRepository roleJpaRepository;
    private final AuthoritiesEntityRepository authorityJpaRepository;
    private final RolePersistenceMapper rolePersistenceMapper;

    @Override
    public boolean existsByCode(String code) {
        return roleJpaRepository.existsByCode(code);
    }

    @Override
    public Optional<RoleAggregate> findById(String roleId) {
        return roleJpaRepository.findById(roleId)
                .map(RolePersistenceMapper::toAggregate);
    }

    @Override
    public void save(RoleAggregate roleAggregate) {
        RolesEntity roleJpaEntity = roleJpaRepository.findById(roleAggregate.getId())
                .orElseGet(RolesEntity::new);
        roleJpaEntity.setId(roleAggregate.getId());
        roleJpaEntity.setCode(roleAggregate.getCode());
        roleJpaEntity.setName(roleAggregate.getName());
        roleJpaEntity.setDescription(roleAggregate.getDescription());
        roleJpaEntity.setEnabled(roleAggregate.getEnabled());
        roleJpaEntity.setCreatedAt(roleAggregate.getCreatedAt());
        roleJpaEntity.setCreatedBy(roleAggregate.getCreatedBy());

        Set<AuthoritiesEntity> authorityJpaEntities = roleAggregate.getAuthorityCodes() == null || roleAggregate.getAuthorityCodes().isEmpty()
                ? new HashSet<>()
                : new HashSet<>(authorityJpaRepository.findByCodeIn(roleAggregate.getAuthorityCodes()));
        roleJpaEntity.setAuthorities(authorityJpaEntities);

        roleJpaRepository.save(roleJpaEntity);
    }
}
