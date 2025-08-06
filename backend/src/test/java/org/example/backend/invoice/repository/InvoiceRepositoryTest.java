package org.example.backend.invoice.repository;

import org.example.backend.invoice.entity.InvoiceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
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
        e1.setInvoiceNumber("INV-11");
        e1.setIssueDate(LocalDate.now());
        e1.setStatus("UNPAID");
        invoiceRepository.save(e1);
        InvoiceEntity e2 = new InvoiceEntity();
        e2.setUserId(userId);
        e2.setCustomerId(customerId);
        e2.setInvoiceNumber("INV-12");
        e2.setIssueDate(LocalDate.now());
        e2.setStatus("UNPAID");
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
        e.setInvoiceNumber("INV-1");
        e.setIssueDate(LocalDate.now());
        e.setStatus("UNPAID");
        e = invoiceRepository.save(e);

        assertTrue(invoiceRepository.findByIdAndUserIdAndCustomerId(e.getId(), userId, customerId).isPresent());
    }
}
