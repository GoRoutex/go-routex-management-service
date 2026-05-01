package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.seat.SearchSeatCommand;
import vn.com.routex.hub.management.service.application.command.seat.SearchSeatResult;
import vn.com.routex.hub.management.service.application.services.RouteSeatService;
import vn.com.routex.hub.management.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.management.service.domain.seat.model.SeatTemplate;
import vn.com.routex.hub.management.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.management.service.domain.seat.port.SeatTemplateRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.cache.redis.models.RouteCacheSeat;
import vn.com.routex.hub.management.service.infrastructure.cache.redis.service.RouteSeatCacheService;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class RouteSeatServiceImpl implements RouteSeatService {

    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final SeatTemplateRepositoryPort seatTemplateRepositoryPort;
    private final RouteSeatCacheService routeSeatCacheService;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public SearchSeatResult searchSeat(SearchSeatCommand command) {

        sLog.info("[SEARCH-SEAT] Search Seat Command: {}", command);
        List<RouteCacheSeat> cacheSeats = routeSeatCacheService.getSeats(command.routeId());

        if(cacheSeats.isEmpty()) {
            List<RouteSeat> seatLists = routeSeatRepositoryPort.findAllByRouteIdOrderBySeatNoAsc(command.routeId());

            if (seatLists.isEmpty()) {
                throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_SEAT_NOT_FOUND, command.routeId())));
            }

            Set<String> seatTemplateIds = seatLists
                    .stream()
                    .map(RouteSeat::getSeatTemplateId)
                    .collect(Collectors.toSet());

            List<SeatTemplate> seatTemplates = seatTemplateRepositoryPort.findAllByIdIn(seatTemplateIds);

            Map<String, SeatTemplate> templateMap = seatTemplates.stream()
                    .collect(Collectors.toMap(
                            SeatTemplate::getId,
                            Function.identity()
                            ));


            sLog.info("[TEST");
            cacheSeats = seatLists.stream()
                    .map(s -> {
                        SeatTemplate seatTemplate = templateMap.get(s.getSeatTemplateId());
                        return RouteCacheSeat.builder()
                                .seatId(s.getId())
                                .seatTemplateId(s.getSeatTemplateId())
                                .routeId(s.getRouteId())
                                .seatNo(s.getSeatNo())
                                .status(s.getStatus())
                                .floor(seatTemplate != null ? seatTemplate.getFloor() : null)
                                .colNo(seatTemplate != null ? seatTemplate.getColumnNo() : 0)
                                .rowNo(seatTemplate != null ? seatTemplate.getRowNo() : 0)
                                .build();
                    })
                    .toList();
            routeSeatCacheService.putSeats(command.routeId(), cacheSeats);
        }
        List<SearchSeatResult.SearchSeatResultData> seats = cacheSeats.stream()
                .map(rs -> SearchSeatResult.SearchSeatResultData.builder()
                        .seatId(rs.seatId())
                        .floor(rs.floor())
                        .code(rs.seatNo())
                        .status(rs.status())
                        .rowNo(rs.rowNo())
                        .colNo(rs.colNo())
                        .build())
                .sorted(Comparator.comparing(SearchSeatResult.SearchSeatResultData::code))
                .toList();

        sLog.info("[SEARCH-SEAT] Search Seat Result Data: {}", seats);
        return SearchSeatResult.builder()
                .data(seats)
                .build();
    }
}
