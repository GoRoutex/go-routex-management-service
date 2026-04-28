package vn.com.routex.hub.management.service.domain.route.readmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RouteSearchView {
    private String id;
    private String merchantId;
    private String vehicleId;
    private String driverId;
    private String routeCode;
    private String pickupBranch;
    private String origin;
    private String destination;
    private OffsetDateTime plannedStartTime;
    private OffsetDateTime plannedEndTime;
}
