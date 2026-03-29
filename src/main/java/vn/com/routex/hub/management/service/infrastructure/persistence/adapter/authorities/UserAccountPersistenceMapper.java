package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import vn.com.routex.hub.management.service.domain.authorities.model.UserAccountReference;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.user.entity.UserEntity;

final class UserAccountPersistenceMapper {

    private UserAccountPersistenceMapper() {
    }

    static UserAccountReference toReference(UserEntity UserEntity) {
        return new UserAccountReference(UserEntity.getId());
    }
}
