package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.repository.VehicleEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VehicleProfileRepositoryAdapter implements VehicleProfileRepositoryPort {

    private final VehicleEntityRepository VehicleEntityRepository;

    @Override
    public boolean existsByVehiclePlate(String vehiclePlate) {
        return VehicleEntityRepository.existsByVehiclePlate(vehiclePlate);
    }

    @Override
    public Optional<VehicleProfile> findById(String id) {
        return VehicleEntityRepository.findById(id).map(VehiclePersistenceMapper::toDomain);
    }

    @Override
    public void save(VehicleProfile vehicleProfile) {
        VehicleEntityRepository.save(VehiclePersistenceMapper.toEntity(vehicleProfile));
    }

    @Override
    public PagedResult<VehicleProfile> fetch(int pageNumber, int pageSize) {
        Page<VehicleEntity> page = VehicleEntityRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<VehicleProfile> items = page.getContent().stream()
                .map(VehiclePersistenceMapper::toDomain)
                .toList();

        return PagedResult.<VehicleProfile>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
