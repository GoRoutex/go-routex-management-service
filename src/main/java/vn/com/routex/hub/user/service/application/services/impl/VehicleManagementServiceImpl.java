package vn.com.routex.hub.user.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.services.VehicleManagementService;
import vn.com.routex.hub.user.service.domain.vehicle.Vehicle;
import vn.com.routex.hub.user.service.domain.vehicle.VehicleRepository;
import vn.com.routex.hub.user.service.domain.vehicle.VehicleType;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleResponse;

import java.time.LocalDateTime;
import java.util.UUID;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_VEHICLE;

@Service
@RequiredArgsConstructor
public class VehicleManagementServiceImpl implements VehicleManagementService {

    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public AddVehicleResponse addVehicle(AddVehicleRequest request) {
        if(vehicleRepository.existsByVehiclePlate(request.getData().getVehiclePlate())) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE, request.getData().getVehiclePlate())));
        }

        Vehicle newVehicle = Vehicle.builder()
                .id(UUID.randomUUID().toString())
                .type(VehicleType.valueOf(request.getData().getType()))
                .vehiclePlate(request.getData().getVehiclePlate())
                .seatCapacity(Integer.valueOf(request.getData().getSeatCapacity()))
                .manufacturer(request.getData().getManufacturer())
                .createdAt(LocalDateTime.now())
                .creator(request.getData().getCreator())
                .createdBy(request.getData().getCreator())
                .build();

        vehicleRepository.save(newVehicle);

        return AddVehicleResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .data(AddVehicleResponse.AddVehicleResponseData.builder()
                        .creator(request.getData().getCreator())
                        .status(newVehicle.getStatus())
                        .type(newVehicle.getType())
                        .vehiclePlate(request.getData().getVehiclePlate())
                        .seatCapacity(request.getData().getSeatCapacity())
                        .manufacturer(request.getData().getManufacturer())
                        .build())
                .build();
    }
}
