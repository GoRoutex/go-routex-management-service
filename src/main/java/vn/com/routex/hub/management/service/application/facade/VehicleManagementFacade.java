package vn.com.routex.hub.management.service.application.facade;

import org.springframework.http.ResponseEntity;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleResponse;

public interface VehicleManagementFacade {
    ResponseEntity<AddVehicleResponse> addVehicle( AddVehicleRequest request);
}
