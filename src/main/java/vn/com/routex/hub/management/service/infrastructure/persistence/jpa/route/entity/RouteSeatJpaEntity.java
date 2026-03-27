package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.seat.SeatStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ROUTE_SEAT")
public class RouteSeatJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "SEAT_NO")
    private String seatNo;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(name = "TICKET_ID")
    private String ticketId;

    @Column(name = "HOLD_UNTIL")
    private OffsetDateTime holdUntil;

    @Column(name = "HOLD_BY")
    private String holdBy;

    @Column(name = "CREATOR")
    private String creator;
}

