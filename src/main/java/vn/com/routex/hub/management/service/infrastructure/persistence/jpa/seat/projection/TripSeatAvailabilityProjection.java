package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.projection;

public interface TripSeatAvailabilityProjection {

    String getTripId();

    Long getAvailableSeat();
}

