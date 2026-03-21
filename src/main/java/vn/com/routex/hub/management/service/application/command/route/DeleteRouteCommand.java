package vn.com.routex.hub.management.service.application.command.route;

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
public class DeleteRouteCommand {
    private String creator;
    private String routeId;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
