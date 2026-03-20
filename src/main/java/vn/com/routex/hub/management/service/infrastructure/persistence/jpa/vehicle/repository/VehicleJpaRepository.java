package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleJpaEntity;

import java.util.List;

@Repository
public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, String> {
    boolean existsByVehiclePlate(String vehiclePlate);

    List<VehicleJpaEntity> findByIdIn(List<String> vehicleIds);
}
