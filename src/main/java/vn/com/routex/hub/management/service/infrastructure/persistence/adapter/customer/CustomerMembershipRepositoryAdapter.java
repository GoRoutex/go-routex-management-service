package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.customer.model.CustomerMembership;
import vn.com.routex.hub.management.service.domain.customer.port.CustomerMembershipRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.customer.repository.CustomerMembershipEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerMembershipRepositoryAdapter implements CustomerMembershipRepositoryPort {

    private final CustomerMembershipEntityRepository customerMembershipEntityRepository;

    @Override
    public Optional<CustomerMembership> findById(String id) {
        return customerMembershipEntityRepository.findById(id).map(CustomerMembershipPersistenceMapper::toDomain);
    }

    @Override
    public CustomerMembership save(CustomerMembership customerMembership) {
        return CustomerMembershipPersistenceMapper.toDomain(customerMembershipEntityRepository.save(CustomerMembershipPersistenceMapper.toJpaEntity(customerMembership)));
    }
}
