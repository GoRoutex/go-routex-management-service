package vn.com.routex.hub.management.service.domain.stoppoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopPointRepository extends JpaRepository<StopPoint, String> {
}
