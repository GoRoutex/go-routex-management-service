package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.entity.OperationPointEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


@Repository
public interface OperationPointEntityRepository extends JpaRepository<OperationPointEntity, String> {
    Optional<OperationPointEntity> findByCode(String code);

    Optional<OperationPointEntity> findByIdAndMerchantId(String id, String merchantId);

    Optional<OperationPointEntity> findByCodeAndMerchantId(String code, String merchantId);

    boolean existsByCode(String code);

    boolean existsByCodeAndMerchantId(String code, String merchantId);

    List<OperationPointEntity> findByMerchantId(String merchantId);

    Page<OperationPointEntity> findByMerchantId(String merchantId, Pageable pageable);
}
