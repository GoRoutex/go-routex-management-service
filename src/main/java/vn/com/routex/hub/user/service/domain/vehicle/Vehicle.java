package vn.com.routex.hub.user.service.domain.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.user.service.domain.auditing.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VEHICLE")
public class Vehicle extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(name = "VEHICLE_PLATE")
    private String vehiclePlate;

    @Column(name = "SEAT_CAPACITY")
    private Integer seatCapacity;

    @Column(name = "MANUFACTURER")
    private String manufacturer;
}
