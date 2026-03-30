package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;

public interface ProvincesManagementService {
    SearchProvincesResult searchProvinces(SearchProvincesQuery query);
}
