package vn.com.routex.hub.management.service.domain.seat.port;


import vn.com.routex.hub.management.service.domain.seat.SeatFloor;
import vn.com.routex.hub.management.service.domain.seat.model.SeatTemplate;

import java.util.List;
import java.util.Optional;

public interface SeatTemplateRepositoryPort {

    List<SeatTemplate> findByVehicleTemplateIdAndFloor(String vehicleTemplateId, SeatFloor floor);

    List<SeatTemplate> findByVehicleTemplateId(String vehicleTemplateId);

    Optional<SeatTemplate> findById(String id);

}
