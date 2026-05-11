package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.specification.TripSpecification;
import vn.com.routex.hub.management.service.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.hub.management.service.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.TripQueryPort;
import vn.com.routex.hub.management.service.domain.trip.TripStatus;
import vn.com.routex.hub.management.service.domain.trip.model.TripAggregate;
import vn.com.routex.hub.management.service.domain.trip.port.TripAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripFetchView;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripSearchView;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TripQueryAdapter implements TripQueryPort {

    private final TripAssignmentRepositoryPort tripAssignmentRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final TripAggregateRepositoryPort tripAggregateRepositoryPort;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public List<TripSearchView> searchAssignedTrips(
            String merchantId,
            String originName,
            String destinationName,
            int pageNumber,
            int pageSize
        ) {

        Specification<TripAggregate> specification = Specification.where(TripSpecification.hasMerchantId(merchantId))
                .and(TripSpecification.originNameContainsIgnoreCase(originName))
                .and(TripSpecification.destinationNameContainsIgnoreCase(destinationName))
//                .and(TripSpecification.hasOriginProvinceId(originProvinceId))
//                .and(TripSpecification.hasDestinationProvinceId(destinationProvinceId))
                .and(TripSpecification.assignedStatus(TripStatus.ASSIGNED));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "departureTime"));
        Page<TripAggregate> tripPage = tripAggregateRepositoryPort.findAll(specification, pageable);
        List<TripAggregate> tripList = tripPage.getContent();

        if(tripList.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> tripIds = tripList.stream()
                .map(TripAggregate::getId)
                .toList();

        List<String> routeIds = tripList.stream()
                .map(TripAggregate::getRouteId)
                .toList();

        Map<String, TripAssignmentRecord> assignmentRecordMap = tripAssignmentRepositoryPort.findLatestActiveByTripIds(tripIds);
        Map<String, RouteAggregate> routeAggregateMap = routeAggregateRepositoryPort.findAllByIdIn(routeIds);

        return tripAggregateRepositoryPort.findAll(specification, pageable)
                .getContent()
                .stream()
                .map(trip -> {
                    TripAssignmentRecord assignmentRecord = assignmentRecordMap.get(trip.getId());
                    RouteAggregate routeAggregate = routeAggregateMap.get(trip.getRouteId());
                    return TripSearchView.builder()
                            .id(trip.getId())
                            .driverId(assignmentRecord.getDriverId())
                            .vehicleId(assignmentRecord.getVehicleId())
                            .routeId(trip.getRouteId())
                            .merchantId(trip.getMerchantId())
                            .tripCode(trip.getTripCode())
                            .pickupBranch(trip.getPickupBranch())
                            .originCode(routeAggregate.getOriginCode())
                            .originName(routeAggregate.getOriginName())
                            .destinationCode(routeAggregate.getDestinationCode())
                            .destinationName(routeAggregate.getDestinationName())
                            .originProvinceId(routeAggregate.getOriginProvinceId())
                            .destinationProvinceId(routeAggregate.getDestinationProvinceId())
                            .originDepartmentId(routeAggregate.getOriginDepartmentId())
                            .destinationDepartmentId(routeAggregate.getDestinationDepartmentId())
                            .departureTime(trip.getDepartureTime())
                            .rawDepartureDate(trip.getRawDepartureDate())
                            .rawDepartureTime(trip.getRawDepartureTime())
                            .durationMinutes(routeAggregate.getDuration())
                            .status(trip.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public PagedResult<TripFetchView> fetchTrips(String merchantId, List<String> merchantIds, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "departureDate"));
        Specification<TripAggregate> specification = Specification.where(TripSpecification.hasMerchantId(merchantId))
                .and(TripSpecification.hasMerchantIds(merchantIds));
        Page<TripAggregate> page = tripAggregateRepositoryPort.findAll(specification, pageable);
        List<TripAggregate> tripList = page.getContent();

        List<String> routeIds = tripList.stream()
                .map(TripAggregate::getRouteId)
                .toList();

        Map<String, RouteAggregate> routeAggregateMap = routeAggregateRepositoryPort.findAllByIdIn(routeIds);

        List<TripFetchView> items = page.getContent().stream()
                .map(trip -> {
                    RouteAggregate routeAggregate = routeAggregateMap.get(trip.getRouteId());
                    return TripFetchView.builder()
                            .id(trip.getId())
                            .tripCode(trip.getTripCode())
                            .routeId(trip.getRouteId())
                            .creator(trip.getCreator())
                            .pickupBranch(trip.getPickupBranch())
                            .originCode(routeAggregate.getOriginCode())
                            .originName(routeAggregate.getOriginName())
                            .destinationCode(routeAggregate.getDestinationCode())
                            .destinationName(routeAggregate.getDestinationName())
                            .originProvinceId(routeAggregate.getOriginProvinceId())
                            .destinationProvinceId(routeAggregate.getDestinationProvinceId())
                            .originDepartmentId(routeAggregate.getOriginDepartmentId())
                            .destinationDepartmentId(routeAggregate.getDestinationDepartmentId())
                            .departureTime(trip.getDepartureTime())
                            .rawDepartureDate(trip.getRawDepartureDate())
                            .rawDepartureTime(trip.getRawDepartureTime())
                            .durationMinutes(routeAggregate.getDuration())
                            .status(trip.getStatus())
                            .build();
                })
                .collect(Collectors.toList());

        return PagedResult.<TripFetchView>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
