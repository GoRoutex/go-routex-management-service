package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.AddVehicleResult;
import vn.com.routex.hub.management.service.application.command.vehicle.DeleteVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.DeleteVehicleResult;
import vn.com.routex.hub.management.service.application.command.vehicle.FetchVehiclesQuery;
import vn.com.routex.hub.management.service.application.command.vehicle.FetchVehiclesResult;
import vn.com.routex.hub.management.service.application.command.vehicle.UpdateVehicleCommand;
import vn.com.routex.hub.management.service.application.command.vehicle.UpdateVehicleResult;

public interface VehicleManagementService {

    AddVehicleResult addVehicle(AddVehicleCommand command);

    UpdateVehicleResult updateVehicle(UpdateVehicleCommand command);

    DeleteVehicleResult deleteVehicle(DeleteVehicleCommand command);

    FetchVehiclesResult fetchVehicles(FetchVehiclesQuery query);
}
