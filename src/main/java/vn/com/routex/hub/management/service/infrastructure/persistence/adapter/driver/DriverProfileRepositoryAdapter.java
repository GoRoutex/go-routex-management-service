package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.driver.model.DriverProfile;
import vn.com.routex.hub.management.service.domain.driver.port.DriverProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.driver.repository.DriverProfileEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverProfileRepositoryAdapter implements DriverProfileRepositoryPort {
    private final DriverProfileEntityRepository driverProfileEntityRepository;
    private final DriverProfilePersistenceMapper driverProfilePersistenceMapper;


    @Override
    public Optional<DriverProfile> findById(String id) {
        return driverProfileEntityRepository.findById(id).map(driverProfilePersistenceMapper::toDomain);
    }

    @Override
    public Optional<DriverProfile> findByUserId(String userId) {
        return driverProfileEntityRepository.findByUserId(userId).map(driverProfilePersistenceMapper::toDomain);
    }

    @Override
    public DriverProfile save(DriverProfile profile) {
        return driverProfilePersistenceMapper.toDomain(driverProfileEntityRepository.save(driverProfilePersistenceMapper.toEntity(profile)));
    }
}
