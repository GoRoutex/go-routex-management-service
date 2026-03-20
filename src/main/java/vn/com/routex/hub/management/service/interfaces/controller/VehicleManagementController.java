package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.management.service.application.dto.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.dto.vehicle.AddVehicleResult;
import vn.com.routex.hub.management.service.application.services.VehicleManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpResponseUtil;
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ADD_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.VEHICLE_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('vehicle:management') and hasRole('ADMIN')")
public class VehicleManagementController {

    private final VehicleManagementService vehicleManagementService;

    @PostMapping(VEHICLE_SERVICE + ADD_PATH)
    public ResponseEntity<AddVehicleResponse> addVehicle(@Valid @RequestBody AddVehicleRequest request) {
        AddVehicleResult result = vehicleManagementService.addVehicle(AddVehicleCommand.builder()
                .creator(request.getData().getCreator())
                .type(request.getData().getType())
                .vehiclePlate(request.getData().getVehiclePlate())
                .seatCapacity(request.getData().getSeatCapacity())
                .manufacturer(request.getData().getManufacturer())
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build());

        AddVehicleResponse response = AddVehicleResponse.builder()
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(AddVehicleResponse.AddVehicleResponseData.builder()
                        .creator(result.getCreator())
                        .type(result.getType())
                        .vehiclePlate(result.getVehiclePlate())
                        .seatCapacity(result.getSeatCapacity())
                        .manufacturer(result.getManufacturer())
                        .status(result.getStatus())
                        .build())
                .build();

        return HttpResponseUtil.buildResponse(request, response);
    }
}
