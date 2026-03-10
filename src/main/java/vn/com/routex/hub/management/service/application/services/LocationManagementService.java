package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.interfaces.models.location.SearchLocationResponse;

public interface LocationManagementService {
    SearchLocationResponse searchLocation(String keyword, int page, int size);
}
