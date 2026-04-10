package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.merchant;

import lombok.experimental.UtilityClass;
import vn.com.routex.hub.management.service.domain.merchant.model.Merchant;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.entity.MerchantEntity;

@UtilityClass
public class MerchantPersistenceMapper {

    public Merchant toDomain(MerchantEntity entity) {
        return Merchant.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .taxCode(entity.getTaxCode())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .representativeName(entity.getRepresentativeName())
                .commissionRate(entity.getCommissionRate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public MerchantEntity toEntity(Merchant merchant) {
        return MerchantEntity.builder()
                .id(merchant.getId())
                .code(merchant.getCode())
                .name(merchant.getName())
                .taxCode(merchant.getTaxCode())
                .phone(merchant.getPhone())
                .email(merchant.getEmail())
                .address(merchant.getAddress())
                .representativeName(merchant.getRepresentativeName())
                .commissionRate(merchant.getCommissionRate())
                .status(merchant.getStatus())
                .createdAt(merchant.getCreatedAt())
                .createdBy(merchant.getCreatedBy())
                .updatedAt(merchant.getUpdatedAt())
                .updatedBy(merchant.getUpdatedBy())
                .build();
    }
}
