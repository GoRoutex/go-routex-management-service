package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository.VehicleJpaRepository;

@Component
@RequiredArgsConstructor
public class JpaVehicleProfileRepositoryAdapter implements VehicleProfileRepositoryPort {

    private final VehicleJpaRepository vehicleJpaRepository;

    @Override
    public boolean existsByVehiclePlate(String vehiclePlate) {
        return vehicleJpaRepository.existsByVehiclePlate(vehiclePlate);
    }

    @Override
    public void save(VehicleProfile vehicleProfile) {
        vehicleJpaRepository.save(VehiclePersistenceMapper.toEntity(vehicleProfile));
    }
}
