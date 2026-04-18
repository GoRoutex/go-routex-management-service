package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.specification.RouteSpecification;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.domain.route.port.RouteQueryPort;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteFetchView;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteSearchView;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity.RouteEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RouteQueryAdapter implements RouteQueryPort {

    private final RouteEntityRepository routeEntityRepository;

    @Override
    public List<RouteSearchView> searchAssignedRoutes(
            String merchantId,
            String origin,
            String destination,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            int pageNumber,
            int pageSize
        ) {
        Specification<RouteEntity> specification = Specification.where(RouteSpecification.hasMerchantId(merchantId))
                .and(RouteSpecification.originContainsIgnoreCase(origin))
                .and(RouteSpecification.destinationContainsIgnoreCase(destination))
                .and(RouteSpecification.plannedStartBetween(startTime, endTime))
                .and(RouteSpecification.assignedStatus(RouteStatus.ASSIGNED));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "plannedStartTime"));

        return routeEntityRepository.findAll(specification, pageable)
                .getContent()
                .stream()
                .map(route -> {
                    RouteSearchView searchView = new RouteSearchView();
                    searchView.setId(route.getId());
                    searchView.setMerchantId(route.getMerchantId());
                    searchView.setRouteCode(route.getRouteCode());
                    searchView.setPickupBranch(route.getPickupBranch());
                    searchView.setOrigin(route.getOrigin());
                    searchView.setDestination(route.getDestination());
                    searchView.setPlannedStartTime(route.getPlannedStartTime());
                    searchView.setPlannedEndTime(route.getPlannedEndTime());
                    return searchView;
                })
                .toList();
    }

    @Override
    public PagedResult<RouteFetchView> fetchRoutes(String merchantId, String merchantName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "plannedStartTime"));
        Specification<RouteEntity> specification = Specification.where(RouteSpecification.hasMerchantId(merchantId))
                .and(RouteSpecification.hasMerchantName(merchantName));
        Page<RouteEntity> page = routeEntityRepository.findAll(specification, pageable);


        List<RouteFetchView> items = page.getContent().stream()
                .map(route -> {
                    RouteFetchView view = new RouteFetchView();
                    view.setId(route.getId());
                    view.setRouteCode(route.getRouteCode());
                    view.setCreator(route.getCreator());
                    view.setPickupBranch(route.getPickupBranch());
                    view.setOrigin(route.getOrigin());
                    view.setDestination(route.getDestination());
                    view.setPlannedStartTime(route.getPlannedStartTime());
                    view.setPlannedEndTime(route.getPlannedEndTime());
                    view.setActualStartTime(route.getActualStartTime());
                    view.setActualEndTime(route.getActualEndTime());
                    view.setStatus(route.getStatus() == null ? null : route.getStatus().name());
                    return view;
                })
                .toList();

        return PagedResult.<RouteFetchView>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
