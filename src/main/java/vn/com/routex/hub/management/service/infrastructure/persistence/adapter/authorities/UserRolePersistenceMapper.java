package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.UserRoleAssignment;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.UserRoleEntityId;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.UserRolesEntity;


@Component
@NoArgsConstructor
final class UserRolePersistenceMapper {
    
    public UserRoleAssignment toAssignment(UserRolesEntity userRoleJpaEntity) {
        return UserRoleAssignment.builder()
                .userId(userRoleJpaEntity.getId().getUserId())
                .roleId(userRoleJpaEntity.getId().getRoleId())
                .assignedAt(userRoleJpaEntity.getAssignedAt())
                .build();
    }

    public UserRolesEntity toEntity(UserRoleAssignment assignment) {
        return UserRolesEntity.builder()
                .id(UserRoleEntityId.builder()
                        .userId(assignment.getUserId())
                        .roleId(assignment.getRoleId())
                        .build())
                .assignedAt(assignment.getAssignedAt())
                .build();
    }
}
