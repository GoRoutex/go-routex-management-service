package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;

import java.util.List;

public interface ProvincesManagementService {
    SearchProvincesResult searchProvinces(SearchProvincesQuery query);

    FetchProvincesResult fetchProvinces(FetchProvincesQuery query);
}
