package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.seat.SeatFloor;
import vn.com.routex.hub.management.service.domain.seat.model.SeatTemplate;
import vn.com.routex.hub.management.service.domain.seat.port.SeatTemplateRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.seat.repository.SeatTemplateEntityRepository;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SeatTemplateRepositoryAdapter implements SeatTemplateRepositoryPort {

    private final SeatTemplateEntityRepository seatTemplateEntityRepository;
    private final SeatTemplatePersistenceMapper seatTemplatePersistenceMapper;

    @Override
    public List<SeatTemplate> findByVehicleTemplateIdAndFloor(String vehicleTemplateId, SeatFloor floor) {
        return seatTemplateEntityRepository.findByVehicleTemplateIdAndFloor(vehicleTemplateId, floor)
                .stream().map(seatTemplatePersistenceMapper::toDomain).toList();
    }

    @Override
    public List<SeatTemplate> findByVehicleTemplateId(String vehicleTemplateId) {
        return seatTemplateEntityRepository.findByVehicleTemplateId(vehicleTemplateId)
                .stream().map(seatTemplatePersistenceMapper::toDomain).toList();
    }

    @Override
    public Optional<SeatTemplate> findById(String id) {
        return seatTemplateEntityRepository.findById(id)
                .map(seatTemplatePersistenceMapper::toDomain);
    }
}
