package vn.com.routex.hub.management.service.domain.operationpoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationPointRepository extends JpaRepository<OperationPoint, String> {
}
