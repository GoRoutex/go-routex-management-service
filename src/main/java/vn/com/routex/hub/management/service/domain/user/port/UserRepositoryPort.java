package vn.com.routex.hub.management.service.domain.user.port;


import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.user.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    PagedResult<User> fetch(int pageNumber, int pageSize);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, String excludedId);

    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, String excludedId);
}
