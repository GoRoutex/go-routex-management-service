package vn.com.routex.hub.management.service.domain.customer.port;

import vn.com.routex.hub.management.service.domain.customer.model.Customer;

import java.util.Optional;

public interface CustomerRepositoryPort {

    Optional<Customer> findByUserId(String userId);
    Customer save(Customer customer);
}
