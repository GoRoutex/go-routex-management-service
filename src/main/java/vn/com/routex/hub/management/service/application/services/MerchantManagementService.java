package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantDetailQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantDetailResult;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsResult;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantCommand;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantResult;

public interface MerchantManagementService {
    FetchMerchantDetailResult fetchMerchantDetail(FetchMerchantDetailQuery query);

    FetchMerchantsResult fetchMerchants(FetchMerchantsQuery query);

    UpdateMerchantResult updateMerchant(UpdateMerchantCommand command);
}
