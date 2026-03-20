package vn.com.routex.hub.management.service.domain.authorities.port;

import vn.com.routex.hub.management.service.domain.authorities.model.RoleAggregate;

import java.util.Optional;

public interface RoleRepositoryPort {
    boolean existsByCode(String code);

    Optional<RoleAggregate> findById(String roleId);

    void save(RoleAggregate roleAggregate);
}
