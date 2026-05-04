package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.specification.TripSpecification;
import vn.com.routex.hub.management.service.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.hub.management.service.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.management.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.management.service.domain.route.port.TripQueryPort;
import vn.com.routex.hub.management.service.domain.trip.TripStatus;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripFetchView;
import vn.com.routex.hub.management.service.domain.trip.readmodel.TripSearchView;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.trip.entity.TripEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.trip.repository.TripEntityRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_ASSIGNMENT_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class TripQueryAdapter implements TripQueryPort {

    private final TripEntityRepository tripEntityRepository;
    private final TripAssignmentRepositoryPort tripAssignmentRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;

    @Override
    public List<TripSearchView> searchAssignedTrips(
            String merchantId,
            String originName,
            String destinationName,
            int pageNumber,
            int pageSize
        ) {
        Specification<TripEntity> specification = Specification.where(TripSpecification.hasMerchantId(merchantId))
                .and(TripSpecification.originNameContainsIgnoreCase(originName))
                .and(TripSpecification.destinationNameContainsIgnoreCase(destinationName))
                .and(TripSpecification.assignedStatus(TripStatus.ASSIGNED));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "departureDate"));

        return tripEntityRepository.findAll(specification, pageable)
                .getContent()
                .stream()
                .map(trip -> {

                    TripAssignmentRecord assignmentRecord = tripAssignmentRepositoryPort.findActiveByTripId(trip.getId(), trip.getMerchantId())
                            .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROUTE_ASSIGNMENT_NOT_FOUND)));

                    RouteAggregate routeAggregate = routeAggregateRepositoryPort.findById(trip.getRouteId())
                            .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, trip.getRouteId()))));

                    return TripSearchView.builder()
                            .id(trip.getId())
                            .driverId(assignmentRecord.getDriverId())
                            .vehicleId(assignmentRecord.getVehicleId())
                            .merchantId(trip.getMerchantId())
                            .tripCode(trip.getTripCode())
                            .pickupBranch(trip.getPickupBranch())
                            .originCode(routeAggregate.getOriginCode())
                            .originName(routeAggregate.getOriginName())
                            .destinationCode(routeAggregate.getDestinationCode())
                            .destinationName(routeAggregate.getDestinationName())
                            .departureTime(trip.getDepartureTime())
                            .rawDepartureDate(trip.getRawDepartureDate())
                            .rawDepartureTime(trip.getRawDepartureTime())
                            .durationMinutes(trip.getDurationMinutes())
                            .status(trip.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public PagedResult<TripFetchView> fetchTrips(String merchantId, List<String> merchantIds, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "departureDate"));
        Specification<TripEntity> specification = Specification.where(TripSpecification.hasMerchantId(merchantId))
                .and(TripSpecification.hasMerchantIds(merchantIds));
        Page<TripEntity> page = tripEntityRepository.findAll(specification, pageable);

        List<TripFetchView> items = page.getContent().stream()
                .map(trip -> {
                    RouteAggregate routeAggregate = routeAggregateRepositoryPort.findById(trip.getRouteId())
                            .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, trip.getRouteId()))));

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
                            .departureTime(trip.getDepartureTime())
                            .rawDepartureDate(trip.getRawDepartureDate())
                            .rawDepartureTime(trip.getRawDepartureTime())
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
