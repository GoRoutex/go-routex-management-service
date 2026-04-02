package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.vehicle;

import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;

final class VehiclePersistenceMapper {

    private VehiclePersistenceMapper() {
    }

    static VehicleEntity toEntity(VehicleProfile vehicleProfile) {
        return VehicleEntity.builder()
                .id(vehicleProfile.getId())
                .creator(vehicleProfile.getCreator())
                .status(vehicleProfile.getStatus())
                .type(vehicleProfile.getType())
                .vehiclePlate(vehicleProfile.getVehiclePlate())
                .seatCapacity(vehicleProfile.getSeatCapacity())
                .hasFloor(vehicleProfile.isHasFloor())
                .manufacturer(vehicleProfile.getManufacturer())
                .createdAt(vehicleProfile.getCreatedAt())
                .createdBy(vehicleProfile.getCreatedBy())
                .build();
    }
}
