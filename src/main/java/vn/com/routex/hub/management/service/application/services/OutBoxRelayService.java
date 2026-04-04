package vn.com.routex.hub.management.service.application.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OutBoxRelayService {

    void pollingEvent() throws JsonProcessingException;
}
