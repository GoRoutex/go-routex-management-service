package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleResult;
import vn.com.routex.hub.management.service.application.command.vehicle.DeleteVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.DeleteVehicleResult;
import vn.com.routex.hub.management.service.application.command.vehicle.FetchVehiclesQuery;
import vn.com.routex.hub.management.service.application.command.vehicle.FetchVehiclesResult;
import vn.com.routex.hub.management.service.application.command.vehicle.UpdateVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.UpdateVehicleResult;
import vn.com.routex.hub.management.service.application.services.VehicleManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.management.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleResponse;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.DeleteVehicleRequest;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.DeleteVehicleResponse;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.FetchVehicleResponse;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.UpdateVehicleRequest;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.UpdateVehicleResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.ADD_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.VEHICLE_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('vehicle:management') or hasRole('ADMIN')")
public class VehicleManagementController {

    private final VehicleManagementService vehicleManagementService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }


    @PostMapping(VEHICLE_SERVICE + ADD_PATH)
    public ResponseEntity<AddVehicleResponse> addVehicle(@Valid @RequestBody AddVehicleRequest request,
                                                         HttpServletRequest servletRequest) {
        sLog.info("[VEHICLE-MANAGEMENT] Add Vehicle Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        AddVehicleResult result = vehicleManagementService.addVehicle(AddVehicleCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .creator(request.getData().getCreator())
                .type(request.getData().getType())
                .vehiclePlate(request.getData().getVehiclePlate())
                .seatCapacity(request.getData().getSeatCapacity())
                .manufacturer(request.getData().getManufacturer())
                .build());


        AddVehicleResponse response = AddVehicleResponse.builder()
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(AddVehicleResponse.AddVehicleResponseData.builder()
                        .id(result.id())
                        .creator(result.creator())
                        .type(result.type())
                        .vehiclePlate(result.vehiclePlate())
                        .seatCapacity(result.seatCapacity())
                        .manufacturer(result.manufacturer())
                        .status(result.status())
                        .build())
                .build();

        sLog.info("[VEHICLE-MANAGEMENT] Add Vehicle Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(VEHICLE_SERVICE + UPDATE_PATH)
    public ResponseEntity<UpdateVehicleResponse> updateVehicle(@Valid @RequestBody UpdateVehicleRequest request,
                                                               HttpServletRequest servletRequest) {
        sLog.info("[VEHICLE-MANAGEMENT] Update Vehicle Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        UpdateVehicleResult result = vehicleManagementService.updateVehicle(UpdateVehicleCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .creator(request.getData().getCreator())
                .vehicleId(request.getData().getVehicleId())
                .type(request.getData().getType())
                .vehiclePlate(request.getData().getVehiclePlate())
                .seatCapacity(request.getData().getSeatCapacity())
                .manufacturer(request.getData().getManufacturer())
                .hasFloor(request.getData().getHasFloor())
                .status(request.getData().getStatus())
                .build());


        UpdateVehicleResponse response = UpdateVehicleResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(UpdateVehicleResponse.UpdateVehicleResponseData.builder()
                        .id(result.id())
                        .creator(result.creator())
                        .type(result.type())
                        .vehiclePlate(result.vehiclePlate())
                        .seatCapacity(result.seatCapacity())
                        .hasFloor(result.hasFloor())
                        .manufacturer(result.manufacturer())
                        .status(result.status())
                        .build())
                .build();

        sLog.info("[VEHICLE-MANAGEMENT] Update Vehicle Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(VEHICLE_SERVICE + DELETE_PATH)
    public ResponseEntity<DeleteVehicleResponse> deleteVehicle(@Valid @RequestBody DeleteVehicleRequest request,
                                                               HttpServletRequest servletRequest) {
        sLog.info("[VEHICLE-MANAGEMENT] Delete Vehicle Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        DeleteVehicleResult result = vehicleManagementService.deleteVehicle(DeleteVehicleCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .creator(request.getData().getCreator())
                .vehicleId(request.getData().getVehicleId())
                .build());


        DeleteVehicleResponse response = DeleteVehicleResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(DeleteVehicleResponse.DeleteVehicleResponseData.builder()
                        .id(result.id())
                        .status(result.status())
                        .build())
                .build();

        sLog.info("[VEHICLE-MANAGEMENT] Delete Vehicle Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(VEHICLE_SERVICE + FETCH_PATH)
    public ResponseEntity<FetchVehicleResponse> fetchVehicles(
            HttpServletRequest servletRequest,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchVehiclesResult result = vehicleManagementService.fetchVehicles(FetchVehiclesQuery.builder()
                .context(HttpUtils.toContext(baseRequest, merchantId))
                .pageNumber(String.valueOf(pageNumber))
                .pageSize(String.valueOf(pageSize))
                .merchantId(merchantId)
                .build());

        List<FetchVehicleResponse.FetchVehicleResponseData> items = result.items().stream()
                .map(v -> FetchVehicleResponse.FetchVehicleResponseData.builder()
                        .id(v.id())
                        .creator(v.creator())
                        .status(v.status())
                        .type(v.type())
                        .vehiclePlate(v.vehiclePlate())
                        .seatCapacity(v.seatCapacity())
                        .hasFloor(v.hasFloor())
                        .manufacturer(v.manufacturer())
                        .build())
                .collect(Collectors.toList());

        FetchVehicleResponse response = FetchVehicleResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchVehicleResponse.FetchVehicleResponsePage.builder()
                        .items(items)
                        .pagination(FetchVehicleResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }
}
