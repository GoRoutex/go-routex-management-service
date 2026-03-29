package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.projection;

public interface RouteSeatAvailabilityProjection {

    String getRouteId();

    Long getAvailableSeat();
}

