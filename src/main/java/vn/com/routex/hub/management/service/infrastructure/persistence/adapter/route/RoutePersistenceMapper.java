package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.model.RouteStopPlan;
import vn.com.routex.hub.management.service.domain.route.model.VehicleSnapshot;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.assignment.entity.TripAssignmentEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity.RouteEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.routepoint.entity.RoutePointEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.vehicle.entity.VehicleTemplateEntity;

@Component
final class RoutePersistenceMapper {

    public RouteAggregate toAggregate(RouteEntity route) {
        return RouteAggregate.builder()
                .id(route.getId())
                .merchantId(route.getMerchantId())
                .creator(route.getCreator())
                .originCode(route.getOriginCode())
                .originName(route.getOriginName())
                .duration(route.getDuration())
                .destinationCode(route.getDestinationCode())
                .destinationName(route.getDestinationName())
                .originProvinceId(route.getOriginProvinceId())
                .destinationProvinceId(route.getDestinationProvinceId())
                .originDepartmentId(route.getOriginDepartmentId())
                .destinationDepartmentId(route.getDestinationDepartmentId())
                .status(route.getStatus())
                .createdAt(route.getCreatedAt())
                .createdBy(route.getCreatedBy())
                .updatedAt(route.getUpdatedAt())
                .updatedBy(route.getUpdatedBy())
                .build();
    }

    public RouteEntity toEntity(RouteAggregate aggregate) {
        return RouteEntity.builder()
                .id(aggregate.getId())
                .merchantId(aggregate.getMerchantId())
                .duration(aggregate.getDuration())
                .creator(aggregate.getCreator())
                .originCode(aggregate.getOriginCode())
                .originName(aggregate.getOriginName())
                .destinationCode(aggregate.getDestinationCode())
                .destinationName(aggregate.getDestinationName())
                .originProvinceId(aggregate.getOriginProvinceId())
                .destinationProvinceId(aggregate.getDestinationProvinceId())
                .originDepartmentId(aggregate.getOriginDepartmentId())
                .destinationDepartmentId(aggregate.getDestinationDepartmentId())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .createdBy(aggregate.getCreatedBy())
                .updatedAt(aggregate.getUpdatedAt())
                .updatedBy(aggregate.getUpdatedBy())
                .build();
    }

    public RouteStopPlan toStopPlan(RoutePointEntity routeStop) {
        return RouteStopPlan.builder()
                .id(routeStop.getId())
                .routeId(routeStop.getRouteId())
                .creator(routeStop.getCreator())
                .stopOrder(Integer.parseInt(routeStop.getStopOrder()))
                .plannedArrivalTime(routeStop.getPlannedArrivalTime())
                .plannedDepartureTime(routeStop.getPlannedDepartureTime())
                .note(routeStop.getNote())
                .departmentId(routeStop.getDepartmentId())
                .stopName(routeStop.getStopName())
                .stopAddress(routeStop.getStopAddress())
                .stopCity(routeStop.getStopCity())
                .stopLatitude(routeStop.getStopLatitude())
                .stopLongitude(routeStop.getStopLongitude())
                .createdAt(routeStop.getCreatedAt())
                .createdBy(routeStop.getCreatedBy())
                .build();
    }

    public RoutePointEntity toEntity(RouteStopPlan stopPlan) {
        return RoutePointEntity.builder()
                .id(stopPlan.getId())
                .routeId(stopPlan.getRouteId())
                .creator(stopPlan.getCreator())
                .stopOrder(String.valueOf(stopPlan.getStopOrder()))
                .plannedArrivalTime(stopPlan.getPlannedArrivalTime())
                .plannedDepartureTime(stopPlan.getPlannedDepartureTime())
                .note(stopPlan.getNote())
                .departmentId(stopPlan.getDepartmentId())
                .stopName(stopPlan.getStopName())
                .stopAddress(stopPlan.getStopAddress())
                .stopCity(stopPlan.getStopCity())
                .stopLatitude(stopPlan.getStopLatitude())
                .stopLongitude(stopPlan.getStopLongitude())
                .createdAt(stopPlan.getCreatedAt())
                .createdBy(stopPlan.getCreatedBy())
                .build();
    }

    public TripAssignmentRecord toAssignmentRecord(TripAssignmentEntity assignment) {
        return TripAssignmentRecord.builder()
                .id(assignment.getId())
                .merchantId(assignment.getMerchantId())
                .tripId(assignment.getTripId())
                .creator(assignment.getCreator())
                .driverId(assignment.getDriverId())
                .vehicleId(assignment.getVehicleId())
                .assignedAt(assignment.getAssignedAt())
                .ticketPrice(assignment.getTicketPrice())
                .unAssignedAt(assignment.getUnAssignedAt())
                .status(assignment.getStatus())
                .createdAt(assignment.getCreatedAt())
                .createdBy(assignment.getCreatedBy())
                .updatedAt(assignment.getUpdatedAt())
                .updatedBy(assignment.getUpdatedBy())
                .build();
    }

    public TripAssignmentEntity toEntity(TripAssignmentRecord record) {
        return TripAssignmentEntity.builder()
                .id(record.getId())
                .merchantId(record.getMerchantId())
                .tripId(record.getTripId())
                .creator(record.getCreator())
                .driverId(record.getDriverId())
                .vehicleId(record.getVehicleId())
                .assignedAt(record.getAssignedAt())
                .ticketPrice(record.getTicketPrice())
                .unAssignedAt(record.getUnAssignedAt())
                .status(record.getStatus())
                .createdAt(record.getCreatedAt())
                .createdBy(record.getCreatedBy())
                .updatedAt(record.getUpdatedAt())
                .updatedBy(record.getUpdatedBy())
                .build();
    }

    public VehicleSnapshot toVehicleSnapshot(VehicleEntity vehicle, VehicleTemplateEntity template) {
        return VehicleSnapshot.builder()
                .id(vehicle.getId())
                .vehiclePlate(vehicle.getVehiclePlate())
                .seatCapacity(template.getSeatCapacity())
                .hasFloor(template.isHasFloor())
                .build();
    }
}
