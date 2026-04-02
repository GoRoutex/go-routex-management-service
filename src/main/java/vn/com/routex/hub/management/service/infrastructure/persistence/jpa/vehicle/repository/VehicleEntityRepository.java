package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;

import java.util.List;

@Repository
public interface VehicleEntityRepository extends JpaRepository<VehicleEntity, String> {
    boolean existsByVehiclePlate(String vehiclePlate);

    List<VehicleEntity> findByIdIn(List<String> vehicleIds);

    Page<VehicleEntity> findAll(Pageable pageable);
}
