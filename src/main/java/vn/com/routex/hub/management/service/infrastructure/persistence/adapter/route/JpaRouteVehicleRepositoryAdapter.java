package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository.VehicleJpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaRouteVehicleRepositoryAdapter implements RouteVehicleRepositoryPort {

    private final VehicleJpaRepository vehicleJpaRepository;

    @Override
    public Optional<VehicleSnapshot> findById(String vehicleId) {
        return vehicleJpaRepository.findById(vehicleId)
                .map(RoutePersistenceMapper::toVehicleSnapshot);
    }

    @Override
    public Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds) {
        return vehicleJpaRepository.findByIdIn(vehicleIds).stream()
                .map(RoutePersistenceMapper::toVehicleSnapshot)
                .collect(Collectors.toMap(VehicleSnapshot::getId, vehicle -> vehicle));
    }
}
