package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.authorities.model.UserAccountReference;
import vn.com.routex.hub.management.service.domain.authorities.port.UserAccountLookupPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.user.repository.UserJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUserAccountLookupAdapter implements UserAccountLookupPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserAccountReference> findById(String userId) {
        return userJpaRepository.findById(userId)
                .map(UserAccountPersistenceMapper::toReference);
    }
}
