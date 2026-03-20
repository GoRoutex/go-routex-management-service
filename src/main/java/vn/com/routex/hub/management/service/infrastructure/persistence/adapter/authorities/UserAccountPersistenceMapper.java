package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import vn.com.routex.hub.management.service.domain.authorities.model.UserAccountReference;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.user.entity.UserJpaEntity;

final class UserAccountPersistenceMapper {

    private UserAccountPersistenceMapper() {
    }

    static UserAccountReference toReference(UserJpaEntity userJpaEntity) {
        return new UserAccountReference(userJpaEntity.getId());
    }
}
