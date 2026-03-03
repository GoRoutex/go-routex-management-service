package vn.com.routex.hub.user.service.application.services;

import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleResponse;

public interface VehicleManagementService {

    AddVehicleResponse addVehicle(AddVehicleRequest request);

}
