package vn.com.routex.hub.management.service.application.command.merchant;

import lombok.Builder;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;

import java.time.OffsetDateTime;

@Builder
public record FetchMerchantApplicationFormDetailResult(
        String id,
        String formCode,
        String displayName,
        String legalName,
        String taxCode,
        String businessLicense,
        String businessLicenseUrl,
        String country,
        String province,
        String district,
        String city,
        String postalCode,
        String description,
        String slug,
        String merchantId,
        String merchantName,
        String approvedBy,
        OffsetDateTime approvedAt,
        String rejectedBy,
        String rejectionReason,
        ApplicationFormStatus status,
        String submittedBy,
        OffsetDateTime submittedAt,
        ContactResult contact,
        BankInfoResult bankInfo,
        OwnerInfoResult ownerInfo
) {
    @Builder
    public record ContactResult(
            String contactEmail,
            String contactName,
            String contactPhone
    ) {
    }

    @Builder
    public record BankInfoResult(
            String bankAccountName,
            String bankAccountNumber,
            String bankBranch,
            String bankName
    ) {
    }

    @Builder
    public record OwnerInfoResult(
            String ownerEmail,
            String ownerFullName,
            String ownerName,
            String ownerPhone
    ) {
    }
}
