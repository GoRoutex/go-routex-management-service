package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.department;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.department.model.Department;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.department.entity.DepartmentEntity;

@Component
public class DepartmentPersistenceMapper {
    public Department toDomain(DepartmentEntity departmentEntity) {
        if(departmentEntity == null) {
            return null;
        }

        return Department.builder()
                .id(departmentEntity.getId())
                .merchantId(departmentEntity.getMerchantId())
                .code(departmentEntity.getCode())
                .name(departmentEntity.getName())
                .type(departmentEntity.getType())
                .address(departmentEntity.getAddress())
                .city(departmentEntity.getCity())
                .latitude(departmentEntity.getLatitude())
                .longitude(departmentEntity.getLongitude())
                .status(departmentEntity.getStatus())
                .createdAt(departmentEntity.getCreatedAt())
                .createdBy(departmentEntity.getCreatedBy())
                .updatedAt(departmentEntity.getUpdatedAt())
                .updatedBy(departmentEntity.getUpdatedBy())
                .build();
    }

    public DepartmentEntity toEntity(Department department) {
        if(department == null) {
            return null;
        }

        return DepartmentEntity.builder()
                .id(department.getId())
                .merchantId(department.getMerchantId())
                .code(department.getCode())
                .name(department.getName())
                .type(department.getType())
                .address(department.getAddress())
                .city(department.getCity())
                .latitude(department.getLatitude())
                .longitude(department.getLongitude())
                .status(department.getStatus())
                .createdAt(department.getCreatedAt())
                .createdBy(department.getCreatedBy())
                .updatedAt(department.getUpdatedAt())
                .updatedBy(department.getUpdatedBy())
                .build();
    }
}
