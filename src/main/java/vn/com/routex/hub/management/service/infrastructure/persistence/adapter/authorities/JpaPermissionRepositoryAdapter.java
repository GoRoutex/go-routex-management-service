package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.PermissionProfile;
import vn.com.routex.hub.management.service.domain.authorities.port.PermissionRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthorityJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository.AuthorityJpaRepository;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JpaPermissionRepositoryAdapter implements PermissionRepositoryPort {

    private final AuthorityJpaRepository authorityJpaRepository;

    @Override
    public boolean existsByCode(String code) {
        return authorityJpaRepository.existsByCode(code);
    }

    @Override
    public List<PermissionProfile> findByCodes(Set<String> codes) {
        return authorityJpaRepository.findByCodeIn(codes).stream()
                .map(PermissionPersistenceMapper::toProfile)
                .toList();
    }

    @Override
    public void save(PermissionProfile permissionProfile) {
        AuthorityJpaEntity authorityJpaEntity = PermissionPersistenceMapper.toEntity(permissionProfile);
        authorityJpaRepository.save(authorityJpaEntity);
    }
}
