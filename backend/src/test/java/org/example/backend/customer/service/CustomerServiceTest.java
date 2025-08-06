package org.example.backend.customer.service;

import org.example.backend.customer.domain.Customer;
import org.example.backend.customer.dto.CreateCustomerRequest;
import org.example.backend.customer.dto.UpdateCustomerRequest;
import org.example.backend.customer.entity.CustomerEntity;
import org.example.backend.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final CustomerService customerService = new CustomerService(customerRepository);

    @Test
    void createCustomerSavesAndReturnsDomain() {
        UUID userId = UUID.randomUUID();
        CreateCustomerRequest request = new CreateCustomerRequest("John", "123", "j@example.com", "Street");

        CustomerEntity savedEntity = new CustomerEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setUserId(userId);
        savedEntity.setName("John");
        savedEntity.setPhone("123");
        savedEntity.setEmail("j@example.com");
        savedEntity.setAddress("Street");

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(savedEntity);

        Customer result = customerService.createCustomer(userId, request);

        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerRepository).save(captor.capture());
        assertEquals("John", captor.getValue().getName());
        assertEquals(savedEntity.getId().toString(), result.id());
    }

    @Test
    void updateCustomerUpdatesFields() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UpdateCustomerRequest request = new UpdateCustomerRequest("Jane", "456", "j2@example.com", "Road");

        CustomerEntity entity = new CustomerEntity();
        entity.setId(customerId);
        entity.setUserId(userId);
        entity.setName("Old");

        when(customerRepository.findByIdAndUserId(customerId, userId)).thenReturn(Optional.of(entity));
        when(customerRepository.save(entity)).thenReturn(entity);

        Customer result = customerService.updateCustomer(userId, customerId, request);

        assertEquals("Jane", entity.getName());
        verify(customerRepository).save(entity);
        assertEquals(customerId.toString(), result.id());
        assertEquals("Jane", result.name());
    }

    @Test
    void deleteCustomerDeletesEntity() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customerId);
        entity.setUserId(userId);
        entity.setName("Del");

        when(customerRepository.findByIdAndUserId(customerId, userId)).thenReturn(Optional.of(entity));

        Customer result = customerService.deleteCustomer(userId, customerId);

        verify(customerRepository).delete(entity);
        assertEquals(customerId.toString(), result.id());
    }
}
