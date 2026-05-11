package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RouteVehicleRepositoryPort {
    Optional<VehicleProfile> findById(String vehicleId);

    Optional<VehicleProfile> findById(String vehicleId, String merchantId);

    Map<String, VehicleProfile> findByIds(List<String> vehicleIds);

    Map<String, VehicleProfile> findByIds(List<String> vehicleIds, String merchantId);
}
