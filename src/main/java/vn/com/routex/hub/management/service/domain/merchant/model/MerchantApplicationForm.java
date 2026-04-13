package vn.com.routex.hub.management.service.domain.merchant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormBankInfo;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormContact;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormOwner;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MerchantApplicationForm extends AbstractAuditingEntity {

    private String id;
    private String displayName;
    private String legalName;
    private ApplicationFormContact contact;
    private ApplicationFormBankInfo bankInfo;
    private ApplicationFormOwner ownerInfo;
    private String approvedBy;
    private String address;
    private OffsetDateTime approvedAt;
    private String businessLicenseUrl;
    private String businessLicense;
    private String country;
    private String formCode;
    private String postalCode;
    private String province;
    private String ward;
    private String description;
    private String rejectedBy;
    private String rejectionReason;
    private ApplicationFormStatus status;
    private OffsetDateTime submittedAt;
    private String submittedBy;
    private String taxCode;
    private String slug;

    public static MerchantApplicationForm submit(
            String id,
            String formCode,
            String displayName,
            String legalName,
            String taxCode,
            String businessLicense,
            String businessLicenseUrl,
            String country,
            String province,
            String address,
            String ward,
            String postalCode,
            String description,
            String slug,
            String contactName,
            String contactPhone,
            String contactEmail,
            String bankName,
            String bankBranch,
            String bankAccountName,
            String bankAccountNumber,
            String ownerName,
            String ownerFullName,
            String ownerPhone,
            String ownerEmail
    ) {
        OffsetDateTime submittedAt = OffsetDateTime.now();
        return MerchantApplicationForm.builder()
                .id(id)
                .formCode(formCode)
                .displayName(displayName)
                .legalName(legalName)
                .taxCode(taxCode)
                .businessLicense(businessLicense)
                .businessLicenseUrl(businessLicenseUrl)
                .country(country)
                .province(province)
                .address(address)
                .ward(ward)
                .postalCode(postalCode)
                .description(description)
                .slug(slug)
                .contact(new ApplicationFormContact(contactEmail, contactName, contactPhone))
                .bankInfo(new ApplicationFormBankInfo(bankAccountName, bankAccountNumber, bankBranch, bankName))
                .ownerInfo(new ApplicationFormOwner(ownerEmail, ownerFullName, ownerName, ownerPhone))
                .status(ApplicationFormStatus.SUBMITTED)
                .submittedAt(submittedAt)
                .submittedBy(contactEmail)
                .createdAt(submittedAt)
                .createdBy(contactEmail)
                .build();
    }
}
