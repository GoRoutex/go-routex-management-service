package vn.com.routex.hub.management.service.domain.driver.port;


import vn.com.routex.hub.management.service.domain.driver.model.DriverProfile;

import java.util.Optional;

/**
 * Domain repository port (no Spring Data/JPA dependency).
 * Infrastructure layer provides an adapter implementation.
 */
public interface DriverProfileRepositoryPort {
    Optional<DriverProfile> findById(String id);
    Optional<DriverProfile> findByUserId(String userId);

    DriverProfile save(DriverProfile profile);
}
