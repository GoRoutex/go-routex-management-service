package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository.VehicleEntityRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteVehicleRepositoryAdapter implements RouteVehicleRepositoryPort {

    private final VehicleEntityRepository VehicleEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public Optional<VehicleSnapshot> findById(String vehicleId) {
        return VehicleEntityRepository.findById(vehicleId)
                .map(routePersistenceMapper::toVehicleSnapshot);
    }

    @Override
    public Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds) {
        return VehicleEntityRepository.findByIdIn(vehicleIds).stream()
                .map(routePersistenceMapper::toVehicleSnapshot)
                .collect(Collectors.toMap(VehicleSnapshot::getId, vehicle -> vehicle));
    }
}
