package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.driver.entity.DriverProfileEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileEntityRepository extends JpaRepository<DriverProfileEntity, String> {

    Optional<DriverProfileEntity> findByUserId(String userId);

    List<DriverProfileEntity> findByMerchantId(String merchantId);
}
