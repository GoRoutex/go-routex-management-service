package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.customer;

import lombok.experimental.UtilityClass;
import vn.com.routex.hub.management.service.domain.customer.model.CustomerMembership;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.customer.entity.CustomerMembershipEntity;

@UtilityClass
public class CustomerMembershipPersistenceMapper {

    public CustomerMembershipEntity toJpaEntity(CustomerMembership customerMembership) {
        return CustomerMembershipEntity
                .builder()
                .id(customerMembership.getId())
                .membershipTierId(customerMembership.getMembershipTierId())
                .currentAvailablePoints(customerMembership.getCurrentAvailablePoints())
                .totalPoints(customerMembership.getTotalPoints())
                .promotedAt(customerMembership.getPromotedAt())
                .status(customerMembership.getStatus())
                .updatedAt(customerMembership.getUpdatedAt())
                .createdAt(customerMembership.getCreatedAt())
                .createdBy(customerMembership.getCreatedBy())
                .updatedBy(customerMembership.getUpdatedBy())
                .build();
    }

    public CustomerMembership toDomain(CustomerMembershipEntity customerMembership) {
        return CustomerMembership
                .builder()
                .id(customerMembership.getId())
                .membershipTierId(customerMembership.getMembershipTierId())
                .currentAvailablePoints(customerMembership.getCurrentAvailablePoints())
                .totalPoints(customerMembership.getTotalPoints())
                .promotedAt(customerMembership.getPromotedAt())
                .status(customerMembership.getStatus())
                .updatedAt(customerMembership.getUpdatedAt())
                .createdAt(customerMembership.getCreatedAt())
                .createdBy(customerMembership.getCreatedBy())
                .updatedBy(customerMembership.getUpdatedBy())
                .build();
    }
}
