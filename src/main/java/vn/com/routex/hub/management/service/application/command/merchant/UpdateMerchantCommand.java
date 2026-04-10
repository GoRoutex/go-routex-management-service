package vn.com.routex.hub.management.service.application.command.merchant;

import lombok.Builder;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;
import vn.com.routex.hub.management.service.domain.merchant.MerchantStatus;

import java.math.BigDecimal;

@Builder
public record UpdateMerchantCommand(
        RequestContext context,
        String merchantId,
        String updatedBy,
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
