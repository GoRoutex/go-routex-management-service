package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity.RouteJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.stoppoint.entity.RouteStopJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleJpaEntity;

final class RoutePersistenceMapper {

    private RoutePersistenceMapper() {
    }

    static RouteAggregate toAggregate(RouteJpaEntity route) {
        return RouteAggregate.builder()
                .id(route.getId())
                .routeCode(route.getRouteCode())
                .creator(route.getCreator())
                .pickupBranch(route.getPickupBranch())
                .origin(route.getOrigin())
                .destination(route.getDestination())
                .plannedStartTime(route.getPlannedStartTime())
                .plannedEndTime(route.getPlannedEndTime())
                .actualStartTime(route.getActualStartTime())
                .actualEndTime(route.getActualEndTime())
                .status(route.getStatus())
                .createdAt(route.getCreatedAt())
                .createdBy(route.getCreatedBy())
                .updatedAt(route.getUpdatedAt())
                .updatedBy(route.getUpdatedBy())
                .build();
    }

    static RouteJpaEntity toEntity(RouteAggregate aggregate) {
        return RouteJpaEntity.builder()
                .id(aggregate.getId())
                .routeCode(aggregate.getRouteCode())
                .creator(aggregate.getCreator())
                .pickupBranch(aggregate.getPickupBranch())
                .origin(aggregate.getOrigin())
                .destination(aggregate.getDestination())
                .plannedStartTime(aggregate.getPlannedStartTime())
                .plannedEndTime(aggregate.getPlannedEndTime())
                .actualStartTime(aggregate.getActualStartTime())
                .actualEndTime(aggregate.getActualEndTime())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .createdBy(aggregate.getCreatedBy())
                .updatedAt(aggregate.getUpdatedAt())
                .updatedBy(aggregate.getUpdatedBy())
                .build();
    }

    static RouteStopPlan toStopPlan(RouteStopJpaEntity routeStop) {
        return RouteStopPlan.builder()
                .id(routeStop.getId())
                .routeId(routeStop.getRouteId())
                .creator(routeStop.getCreator())
                .stopOrder(Integer.parseInt(routeStop.getStopOrder()))
                .plannedArrivalTime(routeStop.getPlannedArrivalTime())
                .plannedDepartureTime(routeStop.getPlannedDepartureTime())
                .note(routeStop.getNote())
                .createdAt(routeStop.getCreatedAt())
                .createdBy(routeStop.getCreatedBy())
                .build();
    }

    static RouteStopJpaEntity toEntity(RouteStopPlan stopPlan) {
        return RouteStopJpaEntity.builder()
                .id(stopPlan.getId())
                .routeId(stopPlan.getRouteId())
                .creator(stopPlan.getCreator())
                .stopOrder(String.valueOf(stopPlan.getStopOrder()))
                .plannedArrivalTime(stopPlan.getPlannedArrivalTime())
                .plannedDepartureTime(stopPlan.getPlannedDepartureTime())
                .note(stopPlan.getNote())
                .createdAt(stopPlan.getCreatedAt())
                .createdBy(stopPlan.getCreatedBy())
                .build();
    }

    static RouteAssignmentRecord toAssignmentRecord(RouteAssignmentJpaEntity assignment) {
        return RouteAssignmentRecord.builder()
                .id(assignment.getId())
                .routeId(assignment.getRouteId())
                .creator(assignment.getCreator())
                .driverId(assignment.getDriverId())
                .vehicleId(assignment.getVehicleId())
                .assignedAt(assignment.getAssignedAt())
                .unAssignedAt(assignment.getUnAssignedAt())
                .status(assignment.getStatus())
                .updatedAt(assignment.getUpdatedAt())
                .updatedBy(assignment.getUpdatedBy())
                .build();
    }

    static RouteAssignmentJpaEntity toEntity(RouteAssignmentRecord record) {
        return RouteAssignmentJpaEntity.builder()
                .id(record.getId())
                .routeId(record.getRouteId())
                .creator(record.getCreator())
                .driverId(record.getDriverId())
                .vehicleId(record.getVehicleId())
                .assignedAt(record.getAssignedAt())
                .unAssignedAt(record.getUnAssignedAt())
                .status(record.getStatus())
                .updatedAt(record.getUpdatedAt())
                .updatedBy(record.getUpdatedBy())
                .build();
    }

    static VehicleSnapshot toVehicleSnapshot(VehicleJpaEntity vehicle) {
        return VehicleSnapshot.builder()
                .id(vehicle.getId())
                .vehiclePlate(vehicle.getVehiclePlate())
                .seatCapacity(vehicle.getSeatCapacity())
                .hasFloor(vehicle.isHasFloor())
                .build();
    }
}
