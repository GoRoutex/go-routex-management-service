package vn.com.routex.hub.management.service.application.dto.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AssignRouteCommand {
    private String creator;
    private String routeId;
    private String vehicleId;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
