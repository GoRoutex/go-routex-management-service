package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.application.command.customer.CustomerMembershipView;
import vn.com.routex.hub.management.service.application.query.CustomerMembershipQueryRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.customer.repository.SpringDataCustomerRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerMembershipQueryAdapter implements CustomerMembershipQueryRepository {

    private final SpringDataCustomerRepository springDataCustomerRepository;

    @Override
    public Optional<CustomerMembershipView> findMembershipSummaryByUserId(String userId) {
        return springDataCustomerRepository.findMembershipSummaryByUserId(userId)
                .map(p -> new CustomerMembershipView(
                        p.getUserId(),
                        p.getTripPoints(),
                        p.getTotalTrips(),
                        p.getTotalSpent(),
                        p.getCurrentTierId(),
                        p.getCurrentBadge(),
                        p.getPriorityLevel(),
                        p.getPointMultiplier(),
                        p.getDiscountPercent()
                ));
    }
}
