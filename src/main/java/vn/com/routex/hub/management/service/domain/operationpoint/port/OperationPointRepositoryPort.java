package vn.com.routex.hub.management.service.domain.operationpoint.port;

import vn.com.routex.hub.management.service.domain.operationpoint.model.OperationPoint;
import vn.com.routex.hub.management.service.domain.common.PagedResult;

import java.util.Optional;

public interface OperationPointRepositoryPort {
    Optional<OperationPoint> findByCode(String code);

    Optional<OperationPoint> findById(String id);

    boolean existsByCode(String code);

    void save(OperationPoint operationPoint);

    PagedResult<OperationPoint> fetch(int pageNumber, int pageSize);
}
