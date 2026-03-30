package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.operationpoint.entity.OperationPointJpaEntity;

import java.util.List;

@Repository
public interface OperationPointJpaRepository extends JpaRepository<OperationPointJpaEntity, String> {

    OperationPointJpaEntity findByRouteId(String routeId);

    List<OperationPointJpaEntity> findAllByRouteId(String routeId);

    List<OperationPointJpaEntity> findByRouteIdIn(List<String> routeIds);
}
