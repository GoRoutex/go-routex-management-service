package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.management.service.domain.merchant.MerchantUserStatus;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.entity.MerchantUserEntity;

import java.util.List;

public interface MerchantUserEntityRepository extends JpaRepository<MerchantUserEntity, String> {

    List<MerchantUserEntity> findByUserIdAndStatus(String userId, MerchantUserStatus status);
}
