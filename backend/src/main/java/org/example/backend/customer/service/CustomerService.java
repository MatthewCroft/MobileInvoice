package org.example.backend.customer.service;

import org.example.backend.customer.domain.Customer;
import org.example.backend.customer.dto.CreateCustomerRequest;
import org.example.backend.customer.dto.UpdateCustomerRequest;
import org.example.backend.customer.entity.CustomerEntity;
import org.example.backend.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(UUID userId, CreateCustomerRequest request) {
        CustomerEntity entity = new CustomerEntity();
        entity.setUserId(userId);
        entity.setName(request.name());
        entity.setPhone(request.phone());
        entity.setEmail(request.email());
        entity.setAddress(request.address());
        CustomerEntity saved = customerRepository.save(entity);
        return toDomain(saved);
    }

    public Customer updateCustomer(UUID userId, UUID customerId, UpdateCustomerRequest request) {
        CustomerEntity entity = customerRepository.findByIdAndUserId(customerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        entity.setName(request.name());
        entity.setPhone(request.phone());
        entity.setEmail(request.email());
        entity.setAddress(request.address());
        CustomerEntity saved = customerRepository.save(entity);
        return toDomain(saved);
    }

    public Customer deleteCustomer(UUID userId, UUID customerId) {
        CustomerEntity entity = customerRepository.findByIdAndUserId(customerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customerRepository.delete(entity);
        return toDomain(entity);
    }

    private Customer toDomain(CustomerEntity entity) {
        return new Customer(
                entity.getId().toString(),
                entity.getName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getAddress()
        );
    }
}
