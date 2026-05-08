package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.department;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.department.model.Department;
import vn.com.routex.hub.management.service.domain.department.port.DepartmentRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.department.entity.DepartmentEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.department.repository.DepartmentEntityRepository;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class DepartmentRepositoryAdapter implements DepartmentRepositoryPort {

    private final DepartmentEntityRepository departmentEntityRepository;
    private final DepartmentPersistenceMapper departmentPersistenceMapper;

    @Override
    public Optional<Department> findById(String id) {
        return departmentEntityRepository.findById(id).map(departmentPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Department> findById(String id, String merchantId) {
        return departmentEntityRepository.findByIdAndMerchantId(id, merchantId)
                .map(departmentPersistenceMapper::toDomain);
    }

    @Override
    public List<Department> findByMerchantId(String merchantId) {
        return departmentEntityRepository.findByMerchantId(merchantId).stream()
                .map(departmentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void save(Department department) {
        departmentEntityRepository.save(departmentPersistenceMapper.toEntity(department));
    }

    @Override
    public PagedResult<Department> fetch(int pageNumber, int pageSize) {
        Page<DepartmentEntity> page = departmentEntityRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return toPagedResult(page);
    }

    @Override
    public PagedResult<Department> fetch(String merchantId, int pageNumber, int pageSize) {
        Page<DepartmentEntity> page = departmentEntityRepository.findByMerchantId(
                merchantId,
                PageRequest.of(pageNumber, pageSize)
        );
        return toPagedResult(page);
    }

    private PagedResult<Department> toPagedResult(Page<DepartmentEntity> page) {
        List<Department> items = page.getContent().stream()
                .map(departmentPersistenceMapper::toDomain)
                .toList();

        return PagedResult.<Department>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
