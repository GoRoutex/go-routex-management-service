package vn.com.routex.hub.management.service.domain.role.port;

import vn.com.routex.hub.management.service.domain.role.model.UserRoles;

import java.util.List;

public interface UserRoleRepositoryPort {

    UserRoles save(UserRoles userRoles);

    List<UserRoles> findByUserId(String userId);
}
