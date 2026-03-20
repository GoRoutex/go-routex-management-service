package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import vn.com.routex.hub.management.service.domain.authorities.model.UserRoleAssignment;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.UserRoleJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.UserRoleJpaId;

final class UserRolePersistenceMapper {

    private UserRolePersistenceMapper() {
    }

    static UserRoleAssignment toAssignment(UserRoleJpaEntity userRoleJpaEntity) {
        return UserRoleAssignment.builder()
                .userId(userRoleJpaEntity.getId().getUserId())
                .roleId(userRoleJpaEntity.getId().getRoleId())
                .assignedAt(userRoleJpaEntity.getAssignedAt())
                .build();
    }

    static UserRoleJpaEntity toEntity(UserRoleAssignment assignment) {
        return UserRoleJpaEntity.builder()
                .id(UserRoleJpaId.builder()
                        .userId(assignment.getUserId())
                        .roleId(assignment.getRoleId())
                        .build())
                .assignedAt(assignment.getAssignedAt())
                .build();
    }
}
