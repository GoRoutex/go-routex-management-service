package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.adapter.vehicle.VehicleTemplatePersistenceMapper;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleTemplateEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository.VehicleEntityRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository.VehicleTemplateRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteVehicleRepositoryAdapter implements RouteVehicleRepositoryPort {

    private final VehicleEntityRepository vehicleEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;
    private final VehicleTemplateRepository vehicleTemplateRepository;
    private final VehicleTemplatePersistenceMapper vehicleTemplatePersistenceMapper;

    @Override
    public Optional<VehicleSnapshot> findById(String vehicleId) {
        return vehicleEntityRepository.findById(vehicleId)
                .flatMap(this::toVehicleSnapshot);
    }

    @Override
    public Optional<VehicleSnapshot> findById(String vehicleId, String merchantId) {
        return vehicleEntityRepository.findByIdAndMerchantId(vehicleId, merchantId)
                .flatMap(this::toVehicleSnapshot);
    }

    @Override
    public Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds) {
        return toVehicleSnapshotMap(vehicleEntityRepository.findByIdIn(vehicleIds));
    }

    @Override
    public Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds, String merchantId) {
        return toVehicleSnapshotMap(vehicleEntityRepository.findByIdInAndMerchantId(vehicleIds, merchantId));
    }

    private Map<String, VehicleSnapshot> toVehicleSnapshotMap(List<VehicleEntity> vehicles) {
        Map<String, VehicleTemplateEntity> templatesById = vehicleTemplateRepository.findAllById(vehicles.stream()
                        .map(VehicleEntity::getTemplateId)
                        .distinct()
                        .toList())
                .stream()
                .collect(Collectors.toMap(VehicleTemplateEntity::getId, Function.identity()));

        return vehicles.stream()
                .map(vehicle -> toVehicleSnapshot(vehicle, templatesById.get(vehicle.getTemplateId())))
                .flatMap(Optional::stream)
                .collect(Collectors.toMap(VehicleSnapshot::getId, Function.identity()));
    }

    private Optional<VehicleSnapshot> toVehicleSnapshot(VehicleEntity vehicle) {
        return vehicleTemplateRepository.findById(vehicle.getTemplateId())
                .map(template -> routePersistenceMapper.toVehicleSnapshot(vehicle, template));
    }

    private Optional<VehicleSnapshot> toVehicleSnapshot(VehicleEntity vehicle, VehicleTemplateEntity template) {
        if (template == null) {
            return Optional.empty();
        }
        return Optional.of(routePersistenceMapper.toVehicleSnapshot(vehicle, template));
    }
}
