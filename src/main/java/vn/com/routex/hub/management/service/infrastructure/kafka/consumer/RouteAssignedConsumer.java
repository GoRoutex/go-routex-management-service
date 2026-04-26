package vn.com.routex.hub.management.service.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.handler.RouteEventHandler;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.management.service.infrastructure.kafka.event.RouteAssignedEvent;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.JsonUtils;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR_MESSAGE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_EVENT_MESSAGE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;

@RequiredArgsConstructor
@Component
public class RouteAssignedConsumer {


    @Value("${spring.kafka.events.route-assigned}")
    private String routeAssignedEvent;


    private final RouteEventHandler routeEventHandler;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @KafkaListener(
            topics = "${spring.kafka.topics.routes}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id.routes}"
    )
    public void routeAssignedConsumer(String payload, Acknowledgment acknowledgment) {
        sLog.info("[ROUTE-ASSIGNED] Raw Payload: {}", payload);

        DomainEvent event = JsonUtils.parseToKafkaObject(
                payload,
                new TypeReference<>() {});


        sLog.info("[ROUTE-ASSIGNED] Domain Event: {}", event);

        if(event == null ||
                event.header() == null ||
                event.payload() == null ||
                event.header().get("context") == null ||
                event.payload().get("data") == null) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_DATA_ERROR_MESSAGE));
        }

        // double check if the event type is matched with the expectation
        if(!routeAssignedEvent.equals(event.eventType())) {
            sLog.info("Ignore Event: {}", event.eventType());
            acknowledgment.acknowledge();
            return;
        }
        BaseRequest context = JsonUtils.convertValue(event.header().get("context"), BaseRequest.class);
        RouteAssignedEvent routeEvent = JsonUtils.convertValue(event.payload().get("data"), RouteAssignedEvent.class);

        sLog.info("[ROUTE-ASSIGNED] Processing event: eventType={} eventId={} aggregateId={} routeId={} vehicleId={} driverId={}",
                event.eventType(),
                event.eventId(),
                event.aggregateId(),
                routeEvent.routeId(),
                routeEvent.vehicleId(),
                routeEvent.driverId());

        sLog.info("[ROUTE-ASSIGNED] Route Assigned Event: {}", routeEvent);


        try {
            validateEvent(event, context, routeEvent);
            routeEventHandler.processAssignedEvent(event, context, routeEvent);

        } catch(Exception e) {
            sLog.error("[ROUTE-EVENT] Failed eventName={} eventId={} aggregateId={} routeId={} vehicleId={}",
                    event.eventType(),
                    event.eventId(),
                    event.aggregateId(),
                    routeEvent.routeId(),
                    routeEvent.vehicleId(),
                    e);
            throw e;
        }

        acknowledgment.acknowledge();
    }


    public void validateEvent(DomainEvent event, BaseRequest context, RouteAssignedEvent data) {
        if (event.eventId().isBlank()
                || event.eventType().isBlank()
                || event.aggregateId().isBlank()
                || context == null
                || context.getRequestId().isBlank()
                || context.getRequestDateTime().isBlank()
                || context.getChannel().isBlank()
                || data.routeId().isBlank()
                || data.vehicleId().isBlank()
                || data.driverId().isBlank()) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(INVALID_EVENT_MESSAGE, event.eventType())));
        }
    }
}
