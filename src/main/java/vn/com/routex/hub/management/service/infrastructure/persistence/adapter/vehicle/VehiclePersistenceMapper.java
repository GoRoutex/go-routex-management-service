package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.vehicle;

import lombok.experimental.UtilityClass;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;


@UtilityClass
public class VehiclePersistenceMapper {

    public VehicleProfile toDomain(VehicleEntity entity) {
        if (entity == null) return null;
        return VehicleProfile.builder()
                .id(entity.getId())
                .merchantId(entity.getMerchantId())
                .templateId(entity.getTemplateId())
                .creator(entity.getCreator())
                .status(entity.getStatus())
                .vehiclePlate(entity.getVehiclePlate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public VehicleEntity toEntity(VehicleProfile vehicleProfile) {
        return VehicleEntity.builder()
                .id(vehicleProfile.getId())
                .merchantId(vehicleProfile.getMerchantId())
                .templateId(vehicleProfile.getTemplateId())
                .creator(vehicleProfile.getCreator())
                .status(vehicleProfile.getStatus())
                .vehiclePlate(vehicleProfile.getVehiclePlate())
                .createdAt(vehicleProfile.getCreatedAt())
                .createdBy(vehicleProfile.getCreatedBy())
                .updatedAt(vehicleProfile.getUpdatedAt())
                .updatedBy(vehicleProfile.getUpdatedBy())
                .build();
    }
}
