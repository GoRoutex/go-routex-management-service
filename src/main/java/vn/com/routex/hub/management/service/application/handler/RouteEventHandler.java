package vn.com.routex.hub.management.service.application.handler;

import vn.com.routex.hub.management.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteAssignedEvent;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

public interface RouteEventHandler {

    void processAssignedEvent(DomainEvent event, BaseRequest context, RouteAssignedEvent assignedEvent);
}
