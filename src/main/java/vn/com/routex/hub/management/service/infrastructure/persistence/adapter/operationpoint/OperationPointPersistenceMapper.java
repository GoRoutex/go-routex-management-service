package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.operationpoint;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.operationpoint.model.OperationPoint;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.entity.OperationPointEntity;

@Component
public class OperationPointPersistenceMapper {
    public OperationPoint toDomain(OperationPointEntity operationPointEntity) {
        if(operationPointEntity == null) {
            return null;
        }

        return OperationPoint.builder()
                .id(operationPointEntity.getId())
                .code(operationPointEntity.getCode())
                .name(operationPointEntity.getName())
                .type(operationPointEntity.getType())
                .address(operationPointEntity.getAddress())
                .city(operationPointEntity.getCity())
                .latitude(operationPointEntity.getLatitude())
                .longitude(operationPointEntity.getLongitude())
                .status(operationPointEntity.getStatus())
                .createdAt(operationPointEntity.getCreatedAt())
                .createdBy(operationPointEntity.getCreatedBy())
                .updatedAt(operationPointEntity.getUpdatedAt())
                .updatedBy(operationPointEntity.getUpdatedBy())
                .build();
    }

    public OperationPointEntity toEntity(OperationPoint operationPoint) {
        if(operationPoint == null) {
            return null;
        }

        return OperationPointEntity.builder()
                .id(operationPoint.getId())
                .code(operationPoint.getCode())
                .name(operationPoint.getName())
                .type(operationPoint.getType())
                .address(operationPoint.getAddress())
                .city(operationPoint.getCity())
                .latitude(operationPoint.getLatitude())
                .longitude(operationPoint.getLongitude())
                .status(operationPoint.getStatus())
                .createdAt(operationPoint.getCreatedAt())
                .createdBy(operationPoint.getCreatedBy())
                .updatedAt(operationPoint.getUpdatedAt())
                .updatedBy(operationPoint.getUpdatedBy())
                .build();
    }
}
