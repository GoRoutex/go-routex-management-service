package vn.com.routex.hub.management.service.domain.seat.port;


import vn.com.routex.hub.management.service.domain.seat.SeatFloor;
import vn.com.routex.hub.management.service.domain.seat.model.SeatTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SeatTemplateRepositoryPort {

    List<SeatTemplate> findByVehicleTemplateIdAndFloor(String vehicleTemplateId, SeatFloor floor);

    List<SeatTemplate> findByVehicleTemplateId(String vehicleTemplateId);

    Optional<SeatTemplate> findById(String id);

    List<SeatTemplate> findAllByIdIn(Set<String> seatTemplateIds);
}
