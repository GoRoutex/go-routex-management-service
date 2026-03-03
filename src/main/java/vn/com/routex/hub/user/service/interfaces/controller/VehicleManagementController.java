package vn.com.routex.hub.user.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.user.service.application.facade.VehicleManagementFacade;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.ADD_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
public class VehicleManagementController {

    private final VehicleManagementFacade vehicleManagementFacade;

    @PostMapping(ADD_PATH)
    public ResponseEntity<AddVehicleResponse> addVehicle(@Valid @RequestBody AddVehicleRequest request) {
        return vehicleManagementFacade.addVehicle(request);
    }
}
