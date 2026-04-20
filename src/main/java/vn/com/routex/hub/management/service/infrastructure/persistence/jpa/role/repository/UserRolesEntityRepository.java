package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.UserRoleEntityId;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.role.entity.UserRolesEntity;

import java.util.List;

public interface UserRolesEntityRepository extends JpaRepository<UserRolesEntity, UserRoleEntityId> {

    List<UserRolesEntity> findByIdUserId(String userId);

    void deleteByIdUserId(String userId);
}
