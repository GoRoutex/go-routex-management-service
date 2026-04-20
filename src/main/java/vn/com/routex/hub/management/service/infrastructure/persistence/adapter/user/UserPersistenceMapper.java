package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.user;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.user.model.User;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.user.entity.UserEntity;

@Component
public class UserPersistenceMapper {

    public UserEntity toJpaEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .passwordHash(user.getPasswordHash())
                .dob(user.getDob())
                .phoneNumber(user.getPhoneNumber())
                .phoneVerified(user.getPhoneVerified())
                .nationalId(user.getNationalId())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .email(user.getEmail())
                .emailVerified(user.getEmailVerified())
                .status(user.getStatus())
                .tenantId(user.getTenantId())
                .language(user.getLanguage())
                .timezone(user.getTimezone())
                .failLoginCount(user.getFailLoginCount())
                .lastLoginAt(user.getLastLoginAt())
                .lockedUntil(user.getLockedUntil())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .profileCompleted(user.getProfileCompleted())
                .build();
    }

    public User toDomain(UserEntity userEntity) {
        if(userEntity == null) {
            return null;
        }
        return User.builder()
                .id(userEntity.getId())
                .passwordHash(userEntity.getPasswordHash())
                .dob(userEntity.getDob())
                .phoneNumber(userEntity.getPhoneNumber())
                .phoneVerified(userEntity.getPhoneVerified())
                .nationalId(userEntity.getNationalId())
                .address(userEntity.getAddress())
                .avatarUrl(userEntity.getAvatarUrl())
                .gender(userEntity.getGender())
                .email(userEntity.getEmail())
                .emailVerified(userEntity.getEmailVerified())
                .status(userEntity.getStatus())
                .tenantId(userEntity.getTenantId())
                .language(userEntity.getLanguage())
                .timezone(userEntity.getTimezone())
                .failLoginCount(userEntity.getFailLoginCount())
                .lastLoginAt(userEntity.getLastLoginAt())
                .lockedUntil(userEntity.getLockedUntil())
                .createdAt(userEntity.getCreatedAt())
                .createdBy(userEntity.getCreatedBy())
                .updatedAt(userEntity.getUpdatedAt())
                .updatedBy(userEntity.getUpdatedBy())
                .profileCompleted(userEntity.getProfileCompleted())
                .build();
    }
}
