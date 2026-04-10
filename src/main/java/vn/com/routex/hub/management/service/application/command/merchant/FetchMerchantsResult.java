package vn.com.routex.hub.management.service.application.command.merchant;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.merchant.MerchantStatus;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record FetchMerchantsResult(
        List<FetchMerchantItemResult> items,
        long totalPartners,
        BigDecimal totalRevenueShare,
        BigDecimal avgRating,
        long numberOfPendingApps,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    @Builder
    public record FetchMerchantItemResult(
            String id,
            String code,
            String name,
            String taxCode,
            String phone,
            String email,
            String address,
            String representativeName,
            BigDecimal commissionRate,
            MerchantStatus status
    ) {
    }
}
