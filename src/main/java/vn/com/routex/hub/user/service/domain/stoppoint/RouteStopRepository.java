package vn.com.routex.hub.user.service.domain.stoppoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, String> {

    RouteStop findByRouteId(String routeId);

    List<RouteStop> findByRouteIdIn(List<String> routeIds);
}
