package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.projection;

public interface RouteSeatAvailabilityProjection {

    String getRouteId();

    Long getAvailableSeat();
}

