package vn.com.routex.hub.management.service.domain.authorities.port;

import vn.com.routex.hub.management.service.domain.authorities.model.UserAccountReference;

import java.util.Optional;

public interface UserAccountLookupPort {
    Optional<UserAccountReference> findById(String userId);
}
