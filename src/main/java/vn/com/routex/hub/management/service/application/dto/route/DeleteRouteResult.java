package vn.com.routex.hub.management.service.application.dto.route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteRouteResult {
    private String creator;
    private String routeId;
    private String routeCode;
    private String status;
    private OffsetDateTime updatedAt;
}
