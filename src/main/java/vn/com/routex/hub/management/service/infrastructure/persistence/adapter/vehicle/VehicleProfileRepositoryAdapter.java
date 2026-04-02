package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository.VehicleEntityRepository;

@Component
@RequiredArgsConstructor
public class VehicleProfileRepositoryAdapter implements VehicleProfileRepositoryPort {

    private final VehicleEntityRepository VehicleEntityRepository;

    @Override
    public boolean existsByVehiclePlate(String vehiclePlate) {
        return VehicleEntityRepository.existsByVehiclePlate(vehiclePlate);
    }

    @Override
    public void save(VehicleProfile vehicleProfile) {
        VehicleEntityRepository.save(VehiclePersistenceMapper.toEntity(vehicleProfile));
    }
}
