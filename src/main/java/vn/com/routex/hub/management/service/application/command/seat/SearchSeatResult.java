package vn.com.routex.hub.management.service.application.command.seat;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.seat.SeatFloor;
import vn.com.routex.hub.management.service.domain.seat.SeatStatus;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record SearchSeatResult(
        List<SearchSeatResultData> data
) {

    @Builder
    public record SearchSeatResultData(
            String seatId,
            SeatFloor floor,
            int colNo,
            int rowNo,
            SeatStatus status,
            String code
    ) {}
}
