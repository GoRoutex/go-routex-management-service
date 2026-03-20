package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.UserRoleAssignment;
import vn.com.routex.hub.management.service.domain.authorities.port.UserRoleAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.UserRoleJpaId;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository.UserRoleJpaRepository;

@Component
@RequiredArgsConstructor
public class JpaUserRoleAssignmentRepositoryAdapter implements UserRoleAssignmentRepositoryPort {

    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public boolean exists(String userId, String roleId) {
        return userRoleJpaRepository.existsById(UserRoleJpaId.builder()
                .userId(userId)
                .roleId(roleId)
                .build());
    }

    @Override
    public void save(UserRoleAssignment assignment) {
        userRoleJpaRepository.save(UserRolePersistenceMapper.toEntity(assignment));
    }
}
