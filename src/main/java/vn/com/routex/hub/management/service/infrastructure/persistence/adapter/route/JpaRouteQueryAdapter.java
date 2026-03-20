package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.specification.RouteSpecification;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;
import vn.com.routex.hub.management.service.domain.route.port.RouteQueryPort;
import vn.com.routex.hub.management.service.domain.route.readmodel.RouteSearchView;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity.RouteJpaEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.repository.RouteJpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaRouteQueryAdapter implements RouteQueryPort {

    private final RouteJpaRepository routeJpaRepository;

    @Override
    public List<RouteSearchView> searchAssignedRoutes(
            String origin,
            String destination,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            int pageNumber,
            int pageSize
    ) {
        Specification<RouteJpaEntity> specification = Specification.where(RouteSpecification.originContainsIgnoreCase(origin))
                .and(RouteSpecification.destinationContainsIgnoreCase(destination))
                .and(RouteSpecification.plannedStartBetween(startTime, endTime))
                .and(RouteSpecification.assignedStatus(RouteStatus.ASSIGNED));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "plannedStartTime"));

        List<RouteSearchView> searchViews = routeJpaRepository.findAll(specification, pageable)
                .getContent()
                .stream()
                .map(route -> {
                    RouteSearchView searchView = new RouteSearchView();
                    searchView.setId(route.getId());
                    searchView.setRouteCode(route.getRouteCode());
                    searchView.setPickupBranch(route.getPickupBranch());
                    searchView.setOrigin(route.getOrigin());
                    searchView.setDestination(route.getDestination());
                    searchView.setPlannedStartTime(route.getPlannedStartTime());
                    searchView.setPlannedEndTime(route.getPlannedEndTime());
                    return searchView;
                })
                .toList();

        return searchViews;
    }
}
