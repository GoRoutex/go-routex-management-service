package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.location.SearchLocationQuery;
import vn.com.routex.hub.management.service.application.command.location.SearchLocationResult;

public interface LocationManagementService {
    SearchLocationResult searchLocation(SearchLocationQuery query);
}
