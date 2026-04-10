package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.operationpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.operationpoint.model.OperationPoint;
import vn.com.routex.hub.management.service.domain.operationpoint.port.OperationPointRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.entity.OperationPointEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.repository.OperationPointEntityRepository;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class OperationPointRepositoryAdapter implements OperationPointRepositoryPort {

    private final OperationPointEntityRepository operationPointEntityRepository;
    private final OperationPointPersistenceMapper operationPointPersistenceMapper;

    @Override
    public Optional<OperationPoint> findByCode(String code) {
        return operationPointEntityRepository.findByCode(code).map(operationPointPersistenceMapper::toDomain);
    }

    @Override
    public Optional<OperationPoint> findByCode(String code, String merchantId) {
        return operationPointEntityRepository.findByCodeAndMerchantId(code, merchantId)
                .map(operationPointPersistenceMapper::toDomain);
    }

    @Override
    public Optional<OperationPoint> findById(String id) {
        return operationPointEntityRepository.findById(id).map(operationPointPersistenceMapper::toDomain);
    }

    @Override
    public Optional<OperationPoint> findById(String id, String merchantId) {
        return operationPointEntityRepository.findByIdAndMerchantId(id, merchantId)
                .map(operationPointPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return operationPointEntityRepository.existsByCode(code);
    }

    @Override
    public boolean existsByCode(String code, String merchantId) {
        return operationPointEntityRepository.existsByCodeAndMerchantId(code, merchantId);
    }

    @Override
    public List<OperationPoint> findByMerchantId(String merchantId) {
        return operationPointEntityRepository.findByMerchantId(merchantId).stream()
                .map(operationPointPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void save(OperationPoint operationPoint) {
        operationPointEntityRepository.save(operationPointPersistenceMapper.toEntity(operationPoint));
    }

    @Override
    public PagedResult<OperationPoint> fetch(int pageNumber, int pageSize) {
        Page<OperationPointEntity> page = operationPointEntityRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return toPagedResult(page);
    }

    @Override
    public PagedResult<OperationPoint> fetch(String merchantId, int pageNumber, int pageSize) {
        Page<OperationPointEntity> page = operationPointEntityRepository.findByMerchantId(
                merchantId,
                PageRequest.of(pageNumber, pageSize)
        );
        return toPagedResult(page);
    }

    private PagedResult<OperationPoint> toPagedResult(Page<OperationPointEntity> page) {
        List<OperationPoint> items = page.getContent().stream()
                .map(operationPointPersistenceMapper::toDomain)
                .toList();

        return PagedResult.<OperationPoint>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
