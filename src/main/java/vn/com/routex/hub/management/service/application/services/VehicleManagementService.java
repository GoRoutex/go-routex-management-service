package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.management.service.interfaces.models.vehicle.AddVehicleResponse;

public interface VehicleManagementService {

    AddVehicleResponse addVehicle(AddVehicleRequest request);

}
