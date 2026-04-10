package vn.com.routex.hub.management.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleResult;
import vn.com.routex.hub.management.service.application.command.vehicle.DeleteVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.DeleteVehicleResult;
import vn.com.routex.hub.management.service.application.command.vehicle.FetchVehiclesQuery;
import vn.com.routex.hub.management.service.application.command.vehicle.FetchVehiclesResult;
import vn.com.routex.hub.management.service.application.command.vehicle.UpdateVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.UpdateVehicleResult;
import vn.com.routex.hub.management.service.application.services.VehicleManagementService;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleType;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_VEHICLE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
public class VehicleManagementServiceImpl implements VehicleManagementService {

    private final VehicleProfileRepositoryPort vehicleProfileRepositoryPort;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    @Transactional
    public AddVehicleResult addVehicle(AddVehicleCommand command) {
        if(vehicleProfileRepositoryPort.existsByVehiclePlate(command.vehiclePlate(), command.merchantId())) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE, command.vehiclePlate())));
        }


        VehicleProfile newVehicle = VehicleProfile.register(
                UUID.randomUUID().toString(),
                command.merchantId(),
                command.creator(),
                VehicleType.valueOf(command.type()),
                command.vehiclePlate(),
                Integer.valueOf(command.seatCapacity()),
                command.manufacturer(),
                OffsetDateTime.now()
        );

        vehicleProfileRepositoryPort.save(newVehicle);

        return AddVehicleResult.builder()
                .id(newVehicle.getId())
                .creator(command.creator())
                .type(newVehicle.getType())
                .vehiclePlate(command.vehiclePlate())
                .seatCapacity(command.seatCapacity())
                .manufacturer(command.manufacturer())
                .status(VehicleStatus.AVAILABLE)
                .build();
    }

    @Override
    @Transactional
    public UpdateVehicleResult updateVehicle(UpdateVehicleCommand command) {
        VehicleProfile existing = vehicleProfileRepositoryPort.findById(command.vehicleId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(VEHICLE_NOT_FOUND_BY_ID, command.vehicleId()))));


        if (command.vehiclePlate() != null && !command.vehiclePlate().isBlank()
                && !command.vehiclePlate().trim().equals(existing.getVehiclePlate())
                && vehicleProfileRepositoryPort.existsByVehiclePlate(command.vehiclePlate().trim(), command.merchantId())) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE, command.vehiclePlate().trim())));
        }


        VehicleProfile updated = existing.toBuilder()
                .merchantId(existing.getMerchantId())
                .creator(ApiRequestUtils.firstNonBlank(command.creator(), existing.getCreator()))
                .type(command.type() == null || command.type().isBlank() ? existing.getType() : VehicleType.valueOf(command.type().trim()))
                .vehiclePlate(ApiRequestUtils.firstNonBlank(command.vehiclePlate(), existing.getVehiclePlate()))
                .seatCapacity(command.seatCapacity() == null || command.seatCapacity().isBlank()
                        ? existing.getSeatCapacity()
                        : Integer.valueOf(command.seatCapacity().trim()))
                .manufacturer(ApiRequestUtils.firstNonBlank(command.manufacturer(), existing.getManufacturer()))
                .hasFloor(command.hasFloor() == null ? existing.isHasFloor() : command.hasFloor())
                .status(command.status() == null ? existing.getStatus() : command.status())
                .build();

        vehicleProfileRepositoryPort.save(updated);

        return UpdateVehicleResult.builder()
                .id(updated.getId())
                .creator(updated.getCreator())
                .type(updated.getType())
                .vehiclePlate(updated.getVehiclePlate())
                .seatCapacity(updated.getSeatCapacity())
                .hasFloor(updated.isHasFloor())
                .manufacturer(updated.getManufacturer())
                .status(updated.getStatus())
                .build();
    }

    @Override
    @Transactional
    public DeleteVehicleResult deleteVehicle(DeleteVehicleCommand command) {
        VehicleProfile existing = vehicleProfileRepositoryPort.findById(command.vehicleId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(VEHICLE_NOT_FOUND_BY_ID, command.vehicleId()))));


        VehicleProfile inactive = existing.toBuilder()
                .status(VehicleStatus.INACTIVE)
                .build();
        vehicleProfileRepositoryPort.save(inactive);

        return DeleteVehicleResult.builder()
                .id(inactive.getId())
                .status(inactive.getStatus())
                .build();
    }

    @Override
    public FetchVehiclesResult fetchVehicles(FetchVehiclesQuery query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        PagedResult<VehicleProfile> page = vehicleProfileRepositoryPort.fetch(query.merchantId(), pageNumber - 1, pageSize);
        List<FetchVehiclesResult.FetchVehicleItemResult> items = page.getItems().stream()
                .map(v -> FetchVehiclesResult.FetchVehicleItemResult.builder()
                        .id(v.getId())
                        .creator(v.getCreator())
                        .status(v.getStatus())
                        .type(v.getType())
                        .vehiclePlate(v.getVehiclePlate())
                        .seatCapacity(v.getSeatCapacity())
                        .hasFloor(v.isHasFloor())
                        .manufacturer(v.getManufacturer())
                        .build())
                .toList();

        return FetchVehiclesResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
