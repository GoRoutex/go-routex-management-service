package vn.com.routex.hub.management.service.domain.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.management.service.domain.vehicle.VehicleType;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class VehicleProfile {
    private String id;
    private String creator;
    private VehicleStatus status;
    private VehicleType type;
    private String vehiclePlate;
    private Integer seatCapacity;
    private boolean hasFloor;
    private String manufacturer;
    private OffsetDateTime createdAt;
    private String createdBy;

    public static VehicleProfile register(
            String id,
            String creator,
            VehicleType type,
            String vehiclePlate,
            Integer seatCapacity,
            String manufacturer,
            OffsetDateTime createdAt
    ) {
        return VehicleProfile.builder()
                .id(id)
                .creator(creator)
                .status(VehicleStatus.AVAILABLE)
                .type(type)
                .vehiclePlate(vehiclePlate)
                .seatCapacity(seatCapacity)
                .manufacturer(manufacturer)
                .createdAt(createdAt)
                .createdBy(creator)
                .build();
    }
}
