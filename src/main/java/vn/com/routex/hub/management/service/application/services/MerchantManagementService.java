package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsResult;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantCommand;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantResult;

public interface MerchantManagementService {
    FetchMerchantsResult fetchMerchants(FetchMerchantsQuery query);

    UpdateMerchantResult updateMerchant(UpdateMerchantCommand command);
}
