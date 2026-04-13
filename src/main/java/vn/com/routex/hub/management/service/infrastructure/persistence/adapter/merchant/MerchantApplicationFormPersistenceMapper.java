package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.merchant;

import lombok.experimental.UtilityClass;
import vn.com.routex.hub.management.service.domain.merchant.model.MerchantApplicationForm;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.entity.MerchantApplicationFormEntity;

@UtilityClass
public class MerchantApplicationFormPersistenceMapper {

    public MerchantApplicationForm toDomain(MerchantApplicationFormEntity entity) {
        return MerchantApplicationForm.builder()
                .id(entity.getId())
                .displayName(entity.getDisplayName())
                .legalName(entity.getLegalName())
                .contact(entity.getContact())
                .bankInfo(entity.getBankInfo())
                .ownerInfo(entity.getOwnerInfo())
                .approvedBy(entity.getApprovedBy())
                .approvedAt(entity.getApprovedAt())
                .businessLicenseUrl(entity.getBusinessLicenseUrl())
                .businessLicense(entity.getBusinessLicense())
                .address(entity.getAddress())
                .ward(entity.getWard())
                .country(entity.getCountry())
                .description(entity.getDescription())
                .formCode(entity.getFormCode())
                .postalCode(entity.getPostalCode())
                .province(entity.getProvince())
                .rejectedBy(entity.getRejectedBy())
                .rejectionReason(entity.getRejectionReason())
                .status(entity.getStatus())
                .submittedAt(entity.getSubmittedAt())
                .submittedBy(entity.getSubmittedBy())
                .taxCode(entity.getTaxCode())
                .slug(entity.getSlug())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public MerchantApplicationFormEntity toEntity(MerchantApplicationForm domain) {
        MerchantApplicationFormEntity entity = new MerchantApplicationFormEntity();
        entity.setId(domain.getId());
        entity.setDisplayName(domain.getDisplayName());
        entity.setLegalName(domain.getLegalName());
        entity.setContact(domain.getContact());
        entity.setBankInfo(domain.getBankInfo());
        entity.setOwnerInfo(domain.getOwnerInfo());
        entity.setApprovedBy(domain.getApprovedBy());
        entity.setApprovedAt(domain.getApprovedAt());
        entity.setBusinessLicenseUrl(domain.getBusinessLicenseUrl());
        entity.setBusinessLicense(domain.getBusinessLicense());
        entity.setWard(domain.getWard());
        entity.setAddress(domain.getAddress());
        entity.setCountry(domain.getCountry());
        entity.setDescription(domain.getDescription());
        entity.setFormCode(domain.getFormCode());
        entity.setPostalCode(domain.getPostalCode());
        entity.setProvince(domain.getProvince());
        entity.setRejectedBy(domain.getRejectedBy());
        entity.setRejectionReason(domain.getRejectionReason());
        entity.setStatus(domain.getStatus());
        entity.setSubmittedAt(domain.getSubmittedAt());
        entity.setSubmittedBy(domain.getSubmittedBy());
        entity.setTaxCode(domain.getTaxCode());
        entity.setSlug(domain.getSlug());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        return entity;
    }
}
