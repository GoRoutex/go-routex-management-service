package vn.com.routex.hub.management.service.domain.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.management.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.management.service.domain.booking.BookingSeatStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeat extends AbstractAuditingEntity {
    private String id;
    private String bookingId;
    private String tripId;
    private String seatNo;
    private BigDecimal price;
    private BookingSeatStatus status;
    private String ticketId;
    private String creator;
}
