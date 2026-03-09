package vn.com.routex.hub.user.service.domain.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {
    boolean existsByCode(String code);
}
