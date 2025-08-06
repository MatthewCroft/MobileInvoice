package org.example.backend.customer.repository;

import org.example.backend.customer.entity.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findAllByUserIdReturnsCustomers() {
        UUID userId = UUID.randomUUID();
        CustomerEntity c1 = new CustomerEntity();
        c1.setUserId(userId);
        c1.setName("A");
        customerRepository.save(c1);

        CustomerEntity c2 = new CustomerEntity();
        c2.setUserId(userId);
        c2.setName("B");
        customerRepository.save(c2);

        List<CustomerEntity> result = customerRepository.findAllByUserId(userId);
        assertEquals(2, result.size());
    }

    @Test
    void findByIdAndUserIdReturnsCustomer() {
        UUID userId = UUID.randomUUID();
        CustomerEntity c = new CustomerEntity();
        c.setUserId(userId);
        c.setName("A");
        c = customerRepository.save(c);

        assertTrue(customerRepository.findByIdAndUserId(c.getId(), userId).isPresent());
    }
}
