package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceResult;

public interface ProvincesManagementService {
    SearchProvincesResult searchProvinces(SearchProvincesQuery query);

    FetchProvincesResult fetchProvinces(FetchProvincesQuery query);

    CreateProvinceResult createProvince(CreateProvinceCommand command);

    UpdateProvinceResult updateProvince(UpdateProvinceCommand command);

    DeleteProvinceResult deleteProvince(DeleteProvinceCommand command);
}
