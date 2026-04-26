package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.user.model.User;
import vn.com.routex.hub.management.service.domain.user.model.UserStatus;
import vn.com.routex.hub.management.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.user.entity.UserEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.user.repository.UserEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserEntityRepository userEntityRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public Optional<User> findById(String id) {
        return userEntityRepository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityRepository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }

    @Override
    public PagedResult<User> fetch(int pageNumber, int pageSize) {
        Page<UserEntity> page = userEntityRepository.findByStatusNot(
                UserStatus.DELETED,
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        return PagedResult.<User>builder()
                .items(page.getContent().stream().map(userPersistenceMapper::toDomain).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userEntityRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByPhoneNumberAndIdNot(String phoneNumber, String excludedId) {
        return userEntityRepository.existsByPhoneNumberAndIdNot(phoneNumber, excludedId);
    }


    @Override
    public User save(User user) {
        return userPersistenceMapper.toDomain(userEntityRepository.save(userPersistenceMapper.toJpaEntity(user)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userEntityRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, String excludedId) {
        return userEntityRepository.existsByEmailAndIdNot(email, excludedId);
    }
}
