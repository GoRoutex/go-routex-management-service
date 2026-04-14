package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.merchant;

import lombok.experimental.UtilityClass;
import vn.com.routex.hub.management.service.domain.merchant.model.Merchant;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.entity.MerchantEntity;

@UtilityClass
public class MerchantPersistenceMapper {

    public MerchantEntity toEntity(Merchant merchant) {
        return MerchantEntity.builder()
                .id(merchant.getId())
                .code(merchant.getCode())
                .slug(merchant.getSlug())

                .displayName(merchant.getDisplayName())
                .legalName(merchant.getLegalName())

                .taxCode(merchant.getTaxCode())
                .businessLicenseNumber(merchant.getBusinessLicenseNumber())
                .businessLicenseUrl(merchant.getBusinessLicenseUrl())

                .phone(merchant.getPhone())
                .email(merchant.getEmail())
                .logoUrl(merchant.getLogoUrl())
                .description(merchant.getDescription())

                .address(merchant.getAddress())
                .ward(merchant.getWard())
                .province(merchant.getProvince())
                .country(merchant.getCountry())
                .postalCode(merchant.getPostalCode())

                .representativeName(merchant.getRepresentativeName())

                // contact
                .contactName(merchant.getContactName())
                .contactPhone(merchant.getContactPhone())
                .contactEmail(merchant.getContactEmail())

                // owner
                .ownerFullName(merchant.getOwnerFullName())
                .ownerPhone(merchant.getOwnerPhone())
                .ownerEmail(merchant.getOwnerEmail())

                // bank
                .bankAccountName(merchant.getBankAccountName())
                .bankAccountNumber(merchant.getBankAccountNumber())
                .bankName(merchant.getBankName())
                .bankBranch(merchant.getBankBranch())

                .commissionRate(merchant.getCommissionRate())
                .status(merchant.getStatus())

                .approvedAt(merchant.getApprovedAt())
                .approvedBy(merchant.getApprovedBy())

                .createdAt(merchant.getCreatedAt())
                .createdBy(merchant.getCreatedBy())
                .updatedAt(merchant.getUpdatedAt())
                .updatedBy(merchant.getUpdatedBy())

                .build();
    }

    public Merchant toDomain(MerchantEntity entity) {
        return Merchant.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .slug(entity.getSlug())

                .displayName(entity.getDisplayName())
                .legalName(entity.getLegalName())

                .taxCode(entity.getTaxCode())
                .businessLicenseNumber(entity.getBusinessLicenseNumber())
                .businessLicenseUrl(entity.getBusinessLicenseUrl())

                .phone(entity.getPhone())
                .email(entity.getEmail())
                .logoUrl(entity.getLogoUrl())
                .description(entity.getDescription())

                .address(entity.getAddress())
                .ward(entity.getWard())
                .province(entity.getProvince())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())

                .representativeName(entity.getRepresentativeName())

                // contact
                .contactName(entity.getContactName())
                .contactPhone(entity.getContactPhone())
                .contactEmail(entity.getContactEmail())

                // owner
                .ownerFullName(entity.getOwnerFullName())
                .ownerPhone(entity.getOwnerPhone())
                .ownerEmail(entity.getOwnerEmail())

                // bank
                .bankAccountName(entity.getBankAccountName())
                .bankAccountNumber(entity.getBankAccountNumber())
                .bankName(entity.getBankName())
                .bankBranch(entity.getBankBranch())

                .commissionRate(entity.getCommissionRate())
                .status(entity.getStatus())

                .approvedAt(entity.getApprovedAt())
                .approvedBy(entity.getApprovedBy())

                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())

                .build();
    }
}
