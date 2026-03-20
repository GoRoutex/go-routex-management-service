package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.RoleJpaEntity;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, String> {

    Optional<RoleJpaEntity> findByCode(String code);

    boolean existsByCode(String code);
}
