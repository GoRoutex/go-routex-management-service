package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.customer.model.Customer;
import vn.com.routex.hub.management.service.domain.customer.port.CustomerRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.customer.repository.CustomerEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerEntityRepository customerEntityRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Optional<Customer> findByUserId(String userId) {
        return customerEntityRepository.findByUserId(userId).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Customer save(Customer customer) {
        return customerPersistenceMapper.toDomain(
                customerEntityRepository.save(customerPersistenceMapper.toJpaEntity(customer))
        );
    }
}
