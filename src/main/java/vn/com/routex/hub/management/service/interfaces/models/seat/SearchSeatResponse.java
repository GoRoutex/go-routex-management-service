package vn.com.routex.hub.management.service.interfaces.models.seat;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.seat.SeatFloor;
import vn.com.routex.hub.management.service.domain.seat.SeatStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SearchSeatResponse extends BaseResponse<SearchSeatResponse.SearchSeatResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchSeatResponsePage {
        private List<SearchSeatResponseData> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchSeatResponseData {
        private String seatId;
        private String code;
        private SeatStatus status;
        private SeatFloor floor;
        private int rowNo;
        private int colNo;
    }
}
