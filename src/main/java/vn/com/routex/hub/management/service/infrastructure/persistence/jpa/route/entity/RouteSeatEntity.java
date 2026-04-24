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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ROUTE_SEAT")
public class RouteSeatEntity {

    @Id
    private String id;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "SEAT_NO")
    private String seatNo;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(name = "CREATOR")
    private String creator;
}

