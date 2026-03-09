package vn.com.routex.hub.user.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.services.VehicleManagementService;
import vn.com.routex.hub.user.service.domain.vehicle.Vehicle;
import vn.com.routex.hub.user.service.domain.vehicle.VehicleRepository;
import vn.com.routex.hub.user.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.user.service.domain.vehicle.VehicleType;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_VEHICLE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

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
                .createdAt(OffsetDateTime.now())
                .status(VehicleStatus.AVAILABLE)
                .creator(request.getData().getCreator())
                .createdBy(request.getData().getCreator())
                .build();

        vehicleRepository.save(newVehicle);

        return AddVehicleResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(AddVehicleResponse.AddVehicleResponseData.builder()
                        .creator(request.getData().getCreator())
                        .status(newVehicle.getStatus())
                        .type(newVehicle.getType())
                        .vehiclePlate(request.getData().getVehiclePlate())
                        .seatCapacity(request.getData().getSeatCapacity())
                        .manufacturer(request.getData().getManufacturer())
                        .status(VehicleStatus.AVAILABLE)
                        .build())
                .build();
    }
}
