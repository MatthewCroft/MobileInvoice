package org.example.backend.invoice.repository;

import org.example.backend.invoice.entity.InvoiceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class InvoiceRepositoryTest {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void findAllByUserIdAndCustomerIdReturnsInvoices() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        InvoiceEntity e1 = new InvoiceEntity();
        e1.setUserId(userId);
        e1.setCustomerId(customerId);
        invoiceRepository.save(e1);
        InvoiceEntity e2 = new InvoiceEntity();
        e2.setUserId(userId);
        e2.setCustomerId(customerId);
        invoiceRepository.save(e2);

        List<InvoiceEntity> result = invoiceRepository.findAllByUserIdAndCustomerId(userId, customerId);
        assertEquals(2, result.size());
    }

    @Test
    void findByIdAndUserIdAndCustomerIdReturnsInvoice() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        InvoiceEntity e = new InvoiceEntity();
        e.setUserId(userId);
        e.setCustomerId(customerId);
        e = invoiceRepository.save(e);

        assertTrue(invoiceRepository.findByIdAndUserIdAndCustomerId(e.getId(), userId, customerId).isPresent());
    }
}
