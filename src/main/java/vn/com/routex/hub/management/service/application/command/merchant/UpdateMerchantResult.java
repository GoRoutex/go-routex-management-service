package vn.com.routex.hub.management.service.application.command.merchant;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.merchant.MerchantStatus;

import java.math.BigDecimal;

@Builder
public record UpdateMerchantResult(
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
