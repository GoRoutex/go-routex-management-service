package vn.com.routex.hub.management.service.domain.operationpoint.port;

import vn.com.routex.hub.management.service.domain.operationpoint.model.OperationPoint;
import vn.com.routex.hub.management.service.domain.common.PagedResult;

import java.util.List;
import java.util.Optional;

public interface OperationPointRepositoryPort {
    Optional<OperationPoint> findByCode(String code);

    Optional<OperationPoint> findByCode(String code, String merchantId);

    Optional<OperationPoint> findById(String id);

    Optional<OperationPoint> findById(String id, String merchantId);

    boolean existsByCode(String code);

    boolean existsByCode(String code, String merchantId);

    List<OperationPoint> findByMerchantId(String merchantId);

    void save(OperationPoint operationPoint);

    PagedResult<OperationPoint> fetch(int pageNumber, int pageSize);

    PagedResult<OperationPoint> fetch(String merchantId, int pageNumber, int pageSize);
}
