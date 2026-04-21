package vn.com.routex.hub.management.service.domain.role.port;


import vn.com.routex.hub.management.service.domain.authorities.model.RoleAggregate;

import java.util.Optional;

public interface RoleRepositoryPort {

    Optional<RoleAggregate> findById(String id);

    Optional<RoleAggregate> findByCode(String code);

    boolean existsByCode(String code);

    void save(RoleAggregate roleAggregate);
}
