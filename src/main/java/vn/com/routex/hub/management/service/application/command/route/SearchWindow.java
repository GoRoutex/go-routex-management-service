package vn.com.routex.hub.management.service.application.command.route;

import java.time.OffsetDateTime;

public record SearchWindow(
        OffsetDateTime start,
        OffsetDateTime endExclusive
){
}
