package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

public interface OutBoxService {
    void generateEvent(String aggregateId, String topic, String eventName, String eventKey, Object payload, BaseRequest context);
}
