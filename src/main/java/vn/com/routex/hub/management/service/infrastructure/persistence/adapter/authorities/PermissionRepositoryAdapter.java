package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.PermissionProfile;
import vn.com.routex.hub.management.service.domain.authorities.port.PermissionRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthoritiesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository.AuthoritiesEntityRepository;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepositoryPort {

    private final AuthoritiesEntityRepository authorityJpaRepository;

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
        AuthoritiesEntity authorityJpaEntity = PermissionPersistenceMapper.toEntity(permissionProfile);
        authorityJpaRepository.save(authorityJpaEntity);
    }
}
