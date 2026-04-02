package vn.com.routex.hub.management.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleResult;
import vn.com.routex.hub.management.service.application.services.VehicleManagementService;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleType;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.UUID;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_VEHICLE;
@Service
@RequiredArgsConstructor
public class VehicleManagementServiceImpl implements VehicleManagementService {

    private final VehicleProfileRepositoryPort vehicleProfileRepositoryPort;

    @Override
    @Transactional
    public AddVehicleResult addVehicle(AddVehicleCommand command) {
        if(vehicleProfileRepositoryPort.existsByVehiclePlate(command.vehiclePlate())) {
            throw new BusinessException(command.requestId(), command.requestDateTime(), command.channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE, command.vehiclePlate())));
        }

        VehicleProfile newVehicle = VehicleProfile.register(
                UUID.randomUUID().toString(),
                command.creator(),
                VehicleType.valueOf(command.type()),
                command.vehiclePlate(),
                Integer.valueOf(command.seatCapacity()),
                command.manufacturer(),
                OffsetDateTime.now()
        );

        vehicleProfileRepositoryPort.save(newVehicle);

        return AddVehicleResult.builder()
                .creator(command.creator())
                .type(newVehicle.getType())
                .vehiclePlate(command.vehiclePlate())
                .seatCapacity(command.seatCapacity())
                .manufacturer(command.manufacturer())
                .status(VehicleStatus.AVAILABLE)
                .build();
    }
}
