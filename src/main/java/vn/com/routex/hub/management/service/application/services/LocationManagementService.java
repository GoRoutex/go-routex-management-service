package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.dto.location.SearchLocationQuery;
import vn.com.routex.hub.management.service.application.dto.location.SearchLocationResult;

public interface LocationManagementService {
    SearchLocationResult searchLocation(SearchLocationQuery query);
}
