package vn.com.routex.hub.management.service.interfaces.models.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.merchant.MerchantStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchMerchantsResponse extends BaseResponse<FetchMerchantsResponse.FetchMerchantsResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchMerchantsResponsePage {
        private List<FetchMerchantResponseData> items;
        private long totalPartners;
        private BigDecimal totalRevenueShare;
        private BigDecimal avgRating;
        private long numberOfPendingApps;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchMerchantResponseData {
        private String id;
        private String code;
        private String name;
        private String taxCode;
        private String phone;
        private String email;
        private String address;
        private String representativeName;
        private BigDecimal commissionRate;
        private MerchantStatus status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
