package vn.com.routex.hub.management.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.handler.TripEventHandler;
import vn.com.routex.hub.management.service.domain.assignment.TripAssignmentStatus;
import vn.com.routex.hub.management.service.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.hub.management.service.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.driver.DriverStatus;
import vn.com.routex.hub.management.service.domain.driver.OperationStatus;
import vn.com.routex.hub.management.service.domain.driver.model.DriverProfile;
import vn.com.routex.hub.management.service.domain.driver.port.DriverProfileRepositoryPort;
import vn.com.routex.hub.management.service.domain.trip.TripStatus;
import vn.com.routex.hub.management.service.domain.trip.model.TripAggregate;
import vn.com.routex.hub.management.service.domain.trip.port.TripAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.management.service.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.TripAssignedEvent;
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
public class TripEventHandlerImpl implements TripEventHandler {

    private final VehicleProfileRepositoryPort vehicleRepositoryPort;
    private final DriverProfileRepositoryPort driverProfileRepositoryPort;
    private final TripAssignmentRepositoryPort tripAssignmentRepositoryPort;
    private final TripAggregateRepositoryPort tripAggregateRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public void processAssignedEvent(DomainEvent event, BaseRequest context, TripAssignedEvent assignedEvent) {
        VehicleProfile vehicleProfile = vehicleRepositoryPort.findById(assignedEvent.vehicleId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        DriverProfile driverProfile = driverProfileRepositoryPort.findById(assignedEvent.driverId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, DRIVER_NOT_FOUND_MESSAGE)));

        TripAssignmentRecord tripAssignmentRecord = tripAssignmentRepositoryPort.findByTripIdAndStatus(assignedEvent.tripId(), TripAssignmentStatus.PENDING_ASSIGNMENT)
                        .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROUTE_ASSIGNMENT_NOT_FOUND)));

        TripAggregate tripAggregate = tripAggregateRepositoryPort.findById(assignedEvent.tripId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, assignedEvent.tripId()))));

        sLog.info("[TRIP-ASSIGNED] Processing eventId={} tripId={} vehicleId={} driverId={} vehicleStatus={} driverStatus={} driverOperationStatus={}",
                event.eventId(),
                assignedEvent.tripId(),
                assignedEvent.vehicleId(),
                assignedEvent.driverId(),
                vehicleProfile.getStatus(),
                driverProfile.getStatus(),
                driverProfile.getOperationStatus());

        if (VehicleStatus.IN_SERVICE.equals(vehicleProfile.getStatus())
                && OperationStatus.ON_TRIP.equals(driverProfile.getOperationStatus())
                && TripStatus.ASSIGNED.equals(tripAggregate.getStatus())
                && TripAssignmentStatus.ASSIGNED.equals(tripAssignmentRecord.getStatus())) {
            sLog.info("[TRIP-ASSIGNED] Skip eventId={} tripId={} because vehicle, driver, routes are already assigned",
                    event.eventId(), assignedEvent.tripId());
            return;
        }

        validateTrips(tripAggregate, tripAssignmentRecord, assignedEvent, context);
        validateVehicle(vehicleProfile, assignedEvent, context);
        validateDriver(driverProfile, assignedEvent, context);

        vehicleProfile.setStatus(VehicleStatus.IN_SERVICE);
        driverProfile.setOperationStatus(OperationStatus.ON_TRIP);
        tripAggregate.setStatus(TripStatus.ASSIGNED);
        tripAssignmentRecord.setStatus(TripAssignmentStatus.ASSIGNED);
        tripAssignmentRepositoryPort.save(tripAssignmentRecord);
        tripAggregateRepositoryPort.save(tripAggregate);
        driverProfileRepositoryPort.save(driverProfile);
        vehicleRepositoryPort.save(vehicleProfile);

        sLog.info("[TRIP-ASSIGNED] Updated eventId={} tripId={} vehicleId={} driverId={} vehicleStatus={} driverOperationStatus={}",
                event.eventId(),
                assignedEvent.tripId(),
                assignedEvent.vehicleId(),
                assignedEvent.driverId(),
                vehicleProfile.getStatus(),
                driverProfile.getOperationStatus());

    }

    private void validateTrips(TripAggregate routeAggregate, TripAssignmentRecord tripAssignmentRecord, TripAssignedEvent assignedEvent, BaseRequest context) {
        if(!TripStatus.SCHEDULED.equals(routeAggregate.getStatus())
        || !TripAssignmentStatus.PENDING_ASSIGNMENT.equals(tripAssignmentRecord.getStatus())) {
            throw new BusinessException(
                    context.getRequestId(),
                    context.getRequestDateTime(),
                    context.getChannel(),
                    ExceptionUtils.buildResultResponse(
                            INVALID_INPUT_ERROR,
                            String.format("Trip and Route Assignment with id %s is not yet SCHEDULED & PENDING_ASSIGNMENT",
                                    assignedEvent.tripId())
                    )
            );
        }
    }

    private void validateVehicle(VehicleProfile vehicleProfile, TripAssignedEvent assignedEvent, BaseRequest context) {
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

    private void validateDriver(DriverProfile driverProfile, TripAssignedEvent assignedEvent, BaseRequest context) {
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
