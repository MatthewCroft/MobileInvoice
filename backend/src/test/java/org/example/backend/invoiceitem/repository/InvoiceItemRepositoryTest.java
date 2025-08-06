package org.example.backend.invoiceitem.repository;

import org.example.backend.invoiceitem.entity.InvoiceItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class InvoiceItemRepositoryTest {
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Test
    void findAllByInvoiceIdReturnsItems() {
        UUID invoiceId = UUID.randomUUID();
        InvoiceItemEntity e1 = new InvoiceItemEntity();
        e1.setInvoiceId(invoiceId);
        e1.setDescription("A");
        e1.setQuantity(1);
        e1.setUnitPrice(BigDecimal.ONE);
        e1.setTotalPrice(BigDecimal.ONE);
        invoiceItemRepository.save(e1);

        InvoiceItemEntity e2 = new InvoiceItemEntity();
        e2.setInvoiceId(invoiceId);
        e2.setDescription("B");
        e2.setQuantity(1);
        e2.setUnitPrice(BigDecimal.ONE);
        e2.setTotalPrice(BigDecimal.ONE);
        invoiceItemRepository.save(e2);

        List<InvoiceItemEntity> result = invoiceItemRepository.findAllByInvoiceId(invoiceId);
        assertEquals(2, result.size());
    }

    @Test
    void findByIdAndInvoiceIdReturnsItem() {
        UUID invoiceId = UUID.randomUUID();
        InvoiceItemEntity e = new InvoiceItemEntity();
        e.setInvoiceId(invoiceId);
        e.setDescription("A");
        e.setQuantity(1);
        e.setUnitPrice(BigDecimal.ONE);
        e.setTotalPrice(BigDecimal.ONE);
        e = invoiceItemRepository.save(e);

        assertTrue(invoiceItemRepository.findByIdAndInvoiceId(e.getId(), invoiceId).isPresent());
    }
}
