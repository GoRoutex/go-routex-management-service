package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.entity.MerchantProvinceEntity;

import java.util.Optional;

public interface MerchantProvinceEntityRepository extends JpaRepository<MerchantProvinceEntity, String> {
    boolean existsByMerchantIdAndProvinceId(String merchantId, Integer provinceId);
    Optional<MerchantProvinceEntity> findByMerchantIdAndProvinceId(String merchantId, Integer provinceId);
}
