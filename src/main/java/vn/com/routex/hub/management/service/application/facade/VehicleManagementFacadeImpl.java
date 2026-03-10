package vn.com.routex.hub.management.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.services.VehicleManagementService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpResponseUtil;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleResponse;

@Component
@RequiredArgsConstructor
public class VehicleManagementFacadeImpl implements VehicleManagementFacade {

    private final VehicleManagementService vehicleManagementService;
    @Override
    public ResponseEntity<AddVehicleResponse> addVehicle(AddVehicleRequest request) {
        AddVehicleResponse response = vehicleManagementService.addVehicle(request);
        return HttpResponseUtil.buildResponse(request, response);
    }
}
