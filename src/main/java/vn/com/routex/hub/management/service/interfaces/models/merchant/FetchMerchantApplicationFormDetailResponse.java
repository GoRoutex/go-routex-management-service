package vn.com.routex.hub.management.service.interfaces.models.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchMerchantApplicationFormDetailResponse extends BaseResponse<FetchMerchantApplicationFormDetailResponse.FetchMerchantApplicationFormDetailData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchMerchantApplicationFormDetailData {
        private String id;
        private String formCode;
        private String displayName;
        private String legalName;
        private String taxCode;
        private String businessLicense;
        private String businessLicenseUrl;
        private String description;
        private String logoUrl;
        private String slug;
        private String approvedBy;
        private AddressData address;
        private OffsetDateTime approvedAt;
        private String rejectedBy;
        private String rejectionReason;
        private ApplicationFormStatus status;
        private String submittedBy;
        private OffsetDateTime submittedAt;
        private ContactData contact;
        private BankInfoData bankInfo;
        private OwnerInfoData ownerInfo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AddressData {
        private String country;
        private String province;
        private String address;
        private String ward;
        private String postalCode;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ContactData {
        private String contactEmail;
        private String contactName;
        private String contactPhone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BankInfoData {
        private String bankAccountName;
        private String bankAccountNumber;
        private String bankBranch;
        private String bankName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class OwnerInfoData {
        private String ownerEmail;
        private String ownerFullName;
        private String ownerName;
        private String ownerPhone;
    }
}
