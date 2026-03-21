package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleResult;

public interface VehicleManagementService {

    AddVehicleResult addVehicle(AddVehicleCommand command);

}
