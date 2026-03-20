package vn.com.routex.hub.management.service.domain.vehicle.port;

import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;

public interface VehicleProfileRepositoryPort {
    boolean existsByVehiclePlate(String vehiclePlate);

    void save(VehicleProfile vehicleProfile);
}
