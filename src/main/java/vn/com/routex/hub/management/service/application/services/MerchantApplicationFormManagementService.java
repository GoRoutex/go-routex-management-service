package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantApplicationFormDetailQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantApplicationFormDetailResult;
import vn.com.routex.hub.management.service.application.command.merchant.FetchPendingMerchantApplicationFormsQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchPendingMerchantApplicationFormsResult;

public interface MerchantApplicationFormManagementService {
    FetchPendingMerchantApplicationFormsResult fetchPendingApplicationForms(FetchPendingMerchantApplicationFormsQuery query);

    FetchMerchantApplicationFormDetailResult fetchApplicationFormDetail(FetchMerchantApplicationFormDetailQuery query);
}
