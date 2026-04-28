package vn.com.routex.hub.management.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.handler.RouteEventHandler;
import vn.com.routex.hub.management.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.management.service.domain.driver.DriverStatus;
import vn.com.routex.hub.management.service.domain.driver.OperationStatus;
import vn.com.routex.hub.management.service.domain.driver.model.DriverProfile;
import vn.com.routex.hub.management.service.domain.driver.port.DriverProfileRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteAssignedEvent;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DRIVER_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_ASSIGNMENT_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Component
@RequiredArgsConstructor
public class RouteEventHandlerImpl implements RouteEventHandler {

    private final VehicleProfileRepositoryPort vehicleRepositoryPort;
    private final DriverProfileRepositoryPort driverProfileRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;

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

        RouteAssignmentRecord routeAssignmentRecord = routeAssignmentRepositoryPort.findByRouteIdAndStatus(assignedEvent.routeId(), RouteAssignmentStatus.PENDING_ASSIGNMENT)
                        .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROUTE_ASSIGNMENT_NOT_FOUND)));

        RouteAggregate routeAggregate = routeAggregateRepositoryPort.findById(assignedEvent.routeId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, assignedEvent.routeId()))));

        sLog.info("[ROUTE-ASSIGNED] Processing eventId={} routeId={} vehicleId={} driverId={} vehicleStatus={} driverStatus={} driverOperationStatus={} currentRouteId={}",
                event.eventId(),
                assignedEvent.routeId(),
                assignedEvent.vehicleId(),
                assignedEvent.driverId(),
                vehicleProfile.getStatus(),
                driverProfile.getStatus(),
                driverProfile.getOperationStatus());

        if (VehicleStatus.IN_SERVICE.equals(vehicleProfile.getStatus())
                && OperationStatus.ON_TRIP.equals(driverProfile.getOperationStatus())
                && RouteStatus.ASSIGNED.equals(routeAggregate.getStatus())
                && RouteAssignmentStatus.ASSIGNED.equals(routeAssignmentRecord.getStatus())) {
            sLog.info("[ROUTE-ASSIGNED] Skip eventId={} routeId={} because vehicle, driver, routes are already assigned",
                    event.eventId(), assignedEvent.routeId());
            return;
        }

        validateRoutes(routeAggregate, routeAssignmentRecord, assignedEvent, context);
        validateVehicle(vehicleProfile, assignedEvent, context);
        validateDriver(driverProfile, assignedEvent, context);

        vehicleProfile.setStatus(VehicleStatus.IN_SERVICE);
        driverProfile.setOperationStatus(OperationStatus.ON_TRIP);
        routeAggregate.setStatus(RouteStatus.ASSIGNED);
        routeAssignmentRecord.setStatus(RouteAssignmentStatus.ASSIGNED);
        routeAssignmentRepositoryPort.save(routeAssignmentRecord);
        routeAggregateRepositoryPort.save(routeAggregate);
        driverProfileRepositoryPort.save(driverProfile);
        vehicleRepositoryPort.save(vehicleProfile);

        sLog.info("[ROUTE-ASSIGNED] Updated eventId={} routeId={} vehicleId={} driverId={} vehicleStatus={} driverOperationStatus={} currentRouteId={}",
                event.eventId(),
                assignedEvent.routeId(),
                assignedEvent.vehicleId(),
                assignedEvent.driverId(),
                vehicleProfile.getStatus(),
                driverProfile.getOperationStatus());

    }

    private void validateRoutes(RouteAggregate routeAggregate, RouteAssignmentRecord routeAssignmentRecord, RouteAssignedEvent assignedEvent, BaseRequest context) {
        if(!RouteStatus.PENDING_ASSIGNMENT.equals(routeAggregate.getStatus())
        || !RouteAssignmentStatus.PENDING_ASSIGNMENT.equals(routeAssignmentRecord.getStatus())) {
            throw new BusinessException(
                    context.getRequestId(),
                    context.getRequestDateTime(),
                    context.getChannel(),
                    ExceptionUtils.buildResultResponse(
                            INVALID_INPUT_ERROR,
                            String.format("Route and Route Assignment with id %s is not yet PENDING_ASSIGNMENT",
                                    assignedEvent.routeId())
                    )
            );
        }
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
    }
}
