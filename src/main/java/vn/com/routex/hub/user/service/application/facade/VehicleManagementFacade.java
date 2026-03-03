package vn.com.routex.hub.user.service.application.facade;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleResponse;

public interface VehicleManagementFacade {
    ResponseEntity<AddVehicleResponse> addVehicle( AddVehicleRequest request);
}
