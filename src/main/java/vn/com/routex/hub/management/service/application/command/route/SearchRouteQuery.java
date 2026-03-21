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
public class SearchRouteQuery {
    private String origin;
    private String destination;
    private String departureDate;
    private String seat;
    private String fromTime;
    private String toTime;
    private String pageSize;
    private String pageNumber;
    private String requestId;
    private String requestDateTime;
    private String channel;
}
