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
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.SEAT_TEMPLATE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class RouteSeatServiceImpl implements RouteSeatService {

    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final SeatTemplateRepositoryPort seatTemplateRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public SearchSeatResult searchSeat(SearchSeatCommand command) {
        List<RouteSeat> seatLists = routeSeatRepositoryPort.findAllByRouteIdOrderBySeatNoAsc(command.routeId());

        if (seatLists.isEmpty()) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_SEAT_NOT_FOUND, command.routeId())));
        }
        Map<String, SeatTemplate> templateMap = seatLists
                .stream()
                .map(RouteSeat::getSeatTemplateId)
                .collect(Collectors.toMap(
                        Function.identity(),
                        templateId -> seatTemplateRepositoryPort.findById(templateId)
                                .orElseThrow(() -> new BusinessException(
                                        command.context().requestId(),
                                        command.context().requestDateTime(),
                                        command.context().channel(),
                                        ExceptionUtils.buildResultResponse(
                                                RECORD_NOT_FOUND,
                                                String.format(SEAT_TEMPLATE_NOT_FOUND, templateId)
                                        )))
                ));


        List<SearchSeatResult.SearchSeatResultData> seats = seatLists.stream()
                .map(rs -> {
                    SeatTemplate seatTemplate = templateMap.get(rs.getSeatTemplateId());

                    return SearchSeatResult.SearchSeatResultData.builder()
                            .seatId(rs.getId())
                            .floor(seatTemplate.getFloor())
                            .code(rs.getSeatNo())
                            .status(rs.getStatus())
                            .floor(seatTemplate.getFloor())
                            .rowNo(seatTemplate.getRowNo())
                            .colNo(seatTemplate.getColumnNo())
                            .build();
                })
                .sorted(Comparator.comparing(SearchSeatResult.SearchSeatResultData::code))
                .toList();

        return SearchSeatResult.builder()
                .data(seats)
                .build();
    }
}
