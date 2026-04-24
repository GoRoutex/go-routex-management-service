package vn.com.routex.hub.management.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.handler.RouteEventHandler;
import vn.com.routex.hub.management.service.domain.driver.DriverStatus;
import vn.com.routex.hub.management.service.domain.driver.OperationStatus;
import vn.com.routex.hub.management.service.domain.driver.model.DriverProfile;
import vn.com.routex.hub.management.service.domain.driver.port.DriverProfileRepositoryPort;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteAssignedEvent;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.util.Objects;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DRIVER_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Component
@RequiredArgsConstructor
public class RouteEventHandlerImpl implements RouteEventHandler {

    private final VehicleProfileRepositoryPort vehicleRepositoryPort;
    private final DriverProfileRepositoryPort driverProfileRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public void processAssignedEvent(DomainEvent event, BaseRequest context, RouteAssignedEvent assignedEvent) {
        VehicleProfile vehicleProfile = vehicleRepositoryPort.findById(assignedEvent.vehicleId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));


        DriverProfile driverProfile = driverProfileRepositoryPort.findById(assignedEvent.driverId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, DRIVER_NOT_FOUND_MESSAGE)));

        sLog.info("[ROUTE-ASSIGNED] Processing eventId={} routeId={} vehicleId={} driverId={} vehicleStatus={} driverStatus={} driverOperationStatus={} currentRouteId={}",
                event.eventId(),
                assignedEvent.routeId(),
                assignedEvent.vehicleId(),
                assignedEvent.driverId(),
                vehicleProfile.getStatus(),
                driverProfile.getStatus(),
                driverProfile.getOperationStatus(),
                driverProfile.getCurrentRouteId());


        if (VehicleStatus.IN_SERVICE.equals(vehicleProfile.getStatus())
                && OperationStatus.ON_TRIP.equals(driverProfile.getOperationStatus())
                && Objects.equals(driverProfile.getCurrentRouteId(), assignedEvent.routeId())) {
            sLog.info("[ROUTE-ASSIGNED] Skip eventId={} routeId={} because vehicle and driver are already assigned",
                    event.eventId(), assignedEvent.routeId());
            return;
        }

        validateVehicle(vehicleProfile, assignedEvent, context);
        validateDriver(driverProfile, assignedEvent, context);

        vehicleProfile.setStatus(VehicleStatus.IN_SERVICE);
        driverProfile.setOperationStatus(OperationStatus.ON_TRIP);
        driverProfile.setCurrentRouteId(assignedEvent.routeId());

        driverProfileRepositoryPort.save(driverProfile);
        vehicleRepositoryPort.save(vehicleProfile);

        sLog.info("[ROUTE-ASSIGNED] Updated eventId={} routeId={} vehicleId={} driverId={} vehicleStatus={} driverOperationStatus={} currentRouteId={}",
                event.eventId(),
                assignedEvent.routeId(),
                assignedEvent.vehicleId(),
                assignedEvent.driverId(),
                vehicleProfile.getStatus(),
                driverProfile.getOperationStatus(),
                driverProfile.getCurrentRouteId());

    }

    private void validateVehicle(VehicleProfile vehicleProfile, RouteAssignedEvent assignedEvent, BaseRequest context) {
        if (VehicleStatus.MAINTENANCE.equals(vehicleProfile.getStatus())
                || VehicleStatus.BROKEN.equals(vehicleProfile.getStatus())
                || VehicleStatus.INACTIVE.equals(vehicleProfile.getStatus())) {
            throw new BusinessException(
                    context.getRequestId(),
                    context.getRequestDateTime(),
                    context.getChannel(),
                    ExceptionUtils.buildResultResponse(
                            INVALID_INPUT_ERROR,
                            String.format("Vehicle %s cannot be assigned while status is %s",
                                    assignedEvent.vehicleId(), vehicleProfile.getStatus())
                    )
            );
        }
    }

    private void validateDriver(DriverProfile driverProfile, RouteAssignedEvent assignedEvent, BaseRequest context) {
        if (!DriverStatus.ACTIVE.equals(driverProfile.getStatus())) {
            throw new BusinessException(
                    context.getRequestId(),
                    context.getRequestDateTime(),
                    context.getChannel(),
                    ExceptionUtils.buildResultResponse(
                            INVALID_INPUT_ERROR,
                            String.format("Driver %s cannot be assigned while status is %s",
                                    assignedEvent.driverId(), driverProfile.getStatus())
                    )
            );
        }

        if (OperationStatus.ON_TRIP.equals(driverProfile.getOperationStatus())
                && !Objects.equals(driverProfile.getCurrentRouteId(), assignedEvent.routeId())) {
            throw new BusinessException(
                    context.getRequestId(),
                    context.getRequestDateTime(),
                    context.getChannel(),
                    ExceptionUtils.buildResultResponse(
                            INVALID_INPUT_ERROR,
                            String.format("Driver %s is already on another route %s",
                                    assignedEvent.driverId(), driverProfile.getCurrentRouteId())
                    )
            );
        }

        if (OperationStatus.NOT_AVAILABLE.equals(driverProfile.getOperationStatus())) {
            throw new BusinessException(
                    context.getRequestId(),
                    context.getRequestDateTime(),
                    context.getChannel(),
                    ExceptionUtils.buildResultResponse(
                            INVALID_INPUT_ERROR,
                            String.format("Driver %s is not available for assignment", assignedEvent.driverId())
                    )
            );
        }
    }
}
