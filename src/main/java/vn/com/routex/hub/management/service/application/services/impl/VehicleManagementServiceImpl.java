package vn.com.routex.hub.management.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.dto.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.dto.vehicle.AddVehicleResult;
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
        if(vehicleProfileRepositoryPort.existsByVehiclePlate(command.getVehiclePlate())) {
            throw new BusinessException(command.getRequestId(), command.getRequestDateTime(), command.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE, command.getVehiclePlate())));
        }

        VehicleProfile newVehicle = VehicleProfile.register(
                UUID.randomUUID().toString(),
                command.getCreator(),
                VehicleType.valueOf(command.getType()),
                command.getVehiclePlate(),
                Integer.valueOf(command.getSeatCapacity()),
                command.getManufacturer(),
                OffsetDateTime.now()
        );

        vehicleProfileRepositoryPort.save(newVehicle);

        return AddVehicleResult.builder()
                .creator(command.getCreator())
                .type(newVehicle.getType())
                .vehiclePlate(command.getVehiclePlate())
                .seatCapacity(command.getSeatCapacity())
                .manufacturer(command.getManufacturer())
                .status(VehicleStatus.AVAILABLE)
                .build();
    }
}
