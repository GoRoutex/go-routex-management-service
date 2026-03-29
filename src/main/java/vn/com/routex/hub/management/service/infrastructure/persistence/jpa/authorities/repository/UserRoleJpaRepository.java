package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.UserRoleJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.UserRoleJpaId;

import java.util.List;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoleJpaEntity, UserRoleJpaId> {

    List<UserRoleJpaEntity> findByIdUserId(String userId);

    void deleteByIdUserId(String userId);
}
