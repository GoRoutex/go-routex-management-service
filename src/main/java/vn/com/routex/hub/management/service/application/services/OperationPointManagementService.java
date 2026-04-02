package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.operationpoint.CreateOperationPointCommand;
import vn.com.routex.hub.management.service.application.command.operationpoint.CreateOperationPointResult;
import vn.com.routex.hub.management.service.application.command.operationpoint.DeleteOperationPointCommand;
import vn.com.routex.hub.management.service.application.command.operationpoint.DeleteOperationPointResult;
import vn.com.routex.hub.management.service.application.command.operationpoint.FetchOperationPointQuery;
import vn.com.routex.hub.management.service.application.command.operationpoint.FetchOperationPointResult;
import vn.com.routex.hub.management.service.application.command.operationpoint.UpdateOperationPointCommand;
import vn.com.routex.hub.management.service.application.command.operationpoint.UpdateOperationPointResult;

public interface OperationPointManagementService {
    CreateOperationPointResult createOperationPoint(CreateOperationPointCommand command);

    UpdateOperationPointResult updateOperationPoint(UpdateOperationPointCommand command);

    DeleteOperationPointResult deleteOperationPoint(DeleteOperationPointCommand command);

    FetchOperationPointResult fetchOperationPoint(FetchOperationPointQuery query);
}
