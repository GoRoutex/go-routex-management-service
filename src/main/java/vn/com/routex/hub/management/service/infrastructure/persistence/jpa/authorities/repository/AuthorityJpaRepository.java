package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity.AuthorityJpaEntity;

import java.util.List;
import java.util.Set;

@Repository
public interface AuthorityJpaRepository extends JpaRepository<AuthorityJpaEntity, Integer> {
    boolean existsByCode(String code);

    List<AuthorityJpaEntity> findByCodeIn(Set<String> authoritiesCode);
}
