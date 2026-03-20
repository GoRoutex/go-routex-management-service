package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.stoppoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.stoppoint.entity.RouteStopJpaEntity;

import java.util.List;

@Repository
public interface RouteStopJpaRepository extends JpaRepository<RouteStopJpaEntity, String> {

    RouteStopJpaEntity findByRouteId(String routeId);

    List<RouteStopJpaEntity> findAllByRouteId(String routeId);

    List<RouteStopJpaEntity> findByRouteIdIn(List<String> routeIds);
}
