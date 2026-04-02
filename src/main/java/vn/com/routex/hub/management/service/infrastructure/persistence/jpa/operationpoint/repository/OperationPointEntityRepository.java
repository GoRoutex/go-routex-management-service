package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.entity.OperationPointEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperationPointEntityRepository extends JpaRepository<OperationPointEntity, String> {

    OperationPointEntity findByRouteId(String routeId);

    List<OperationPointEntity> findAllByRouteId(String routeId);

    List<OperationPointEntity> findByRouteIdIn(List<String> routeIds);

    Optional<OperationPointEntity> findByRouteIdAndStopOrder(String routeId, String stopOrder);
}
