package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.UserRoleAssignment;
import vn.com.routex.hub.management.service.domain.authorities.port.UserRoleAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.UserRoleEntityId;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.repository.UserRolesEntityRepository;

@Component
@RequiredArgsConstructor
public class UserRoleAssignmentRepositoryAdapter implements UserRoleAssignmentRepositoryPort {

    private final UserRolesEntityRepository userRoleJpaRepository;
    private final UserRolePersistenceMapper userRolePersistenceMapper;

    @Override
    public boolean exists(String userId, String roleId) {
        return userRoleJpaRepository.existsById(UserRoleEntityId.builder()
                .userId(userId)
                .roleId(roleId)
                .build());
    }

    @Override
    public void save(UserRoleAssignment assignment) {
        userRoleJpaRepository.save(userRolePersistenceMapper.toEntity(assignment));
    }

    @Override
    public void deleteByUserId(String userId) {
        userRoleJpaRepository.deleteByIdUserId(userId);
    }
}
