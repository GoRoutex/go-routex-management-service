package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.dto.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.dto.vehicle.AddVehicleResult;

public interface VehicleManagementService {

    AddVehicleResult addVehicle(AddVehicleCommand command);

}
