package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.user.entity.UserEntity;

import java.util.Optional;

public interface SpringDataCustomerRepository extends JpaRepository<UserEntity, String> {
    @Query("""
        SELECT u.id as userId,
               c.totalTrips as totalTrips,
               c.totalSpent as totalSpent,
               c.tripPoints as tripPoints,
               t.id as currentTierId,
               t.badge as currentBadge,
               t.priorityLevel as priorityLevel,
               t.pointMultiplier as pointMultiplier,
               t.discountPercent as discountPercent
        FROM UserEntity u
        JOIN CustomerEntity c ON u.id = c.userId
        JOIN CustomerMembershipEntity cm ON c.id = cm.customerId
        JOIN MembershipTierEntity t ON cm.membershipTierId = t.id
        WHERE u.id = :userId
    """)
    Optional<CustomerMembershipProjection> findMembershipSummaryByUserId(@Param("userId") String userId);

}
