package org.example.backend.invoice.service;

import org.example.backend.customer.entity.CustomerEntity;
import org.example.backend.customer.repository.CustomerRepository;
import org.example.backend.invoice.domain.Invoice;
import org.example.backend.invoice.dto.CreateInvoiceRequest;
import org.example.backend.invoice.dto.UpdateInvoiceRequest;
import org.example.backend.invoice.entity.InvoiceEntity;
import org.example.backend.invoice.repository.InvoiceRepository;
import org.example.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    private final InvoiceRepository invoiceRepository = mock(InvoiceRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final InvoiceService invoiceService = new InvoiceService(invoiceRepository, userRepository, customerRepository);

    @Test
    void createInvoiceSavesAndReturnsDomain() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        CreateInvoiceRequest request = new CreateInvoiceRequest(
                "INV-1",
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                "unpaid",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.valueOf(11),
                "note"
        );

        when(userRepository.existsById(userId)).thenReturn(true);
        when(customerRepository.findByIdAndUserId(customerId, userId)).thenReturn(Optional.of(new CustomerEntity()));

        InvoiceEntity saved = new InvoiceEntity();
        saved.setId(UUID.randomUUID());
        saved.setUserId(userId);
        saved.setCustomerId(customerId);
        saved.setInvoiceNumber("INV-1");
        saved.setIssueDate(request.issueDate());
        saved.setDueDate(request.dueDate());
        saved.setStatus(request.status());
        saved.setTaxAmount(request.taxAmount());
        saved.setSubtotal(request.subtotal());
        saved.setTotal(request.total());
        saved.setNotes(request.notes());

        when(invoiceRepository.save(any(InvoiceEntity.class))).thenReturn(saved);

        Invoice result = invoiceService.createInvoice(userId, customerId, request);

        ArgumentCaptor<InvoiceEntity> captor = ArgumentCaptor.forClass(InvoiceEntity.class);
        verify(invoiceRepository).save(captor.capture());
        assertEquals("INV-1", captor.getValue().getInvoiceNumber());
        assertEquals(saved.getId().toString(), result.id());
    }

    @Test
    void updateInvoiceUpdatesFields() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UpdateInvoiceRequest request = new UpdateInvoiceRequest(
                "INV-2",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "paid",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.valueOf(11),
                "n2"
        );

        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoiceId);
        entity.setUserId(userId);
        entity.setCustomerId(customerId);
        entity.setInvoiceNumber("OLD");

        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)).thenReturn(Optional.of(entity));
        when(invoiceRepository.save(entity)).thenReturn(entity);

        Invoice result = invoiceService.updateInvoice(userId, customerId, invoiceId, request);

        assertEquals("INV-2", entity.getInvoiceNumber());
        verify(invoiceRepository).save(entity);
        assertEquals(invoiceId.toString(), result.id());
    }

    @Test
    void deleteInvoiceDeletesEntity() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoiceId);
        entity.setUserId(userId);
        entity.setCustomerId(customerId);
        entity.setInvoiceNumber("INV");

        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)).thenReturn(Optional.of(entity));

        Invoice result = invoiceService.deleteInvoice(userId, customerId, invoiceId);

        verify(invoiceRepository).delete(entity);
        assertEquals(invoiceId.toString(), result.id());
    }

    @Test
    void getInvoiceReturnsDomain() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoiceId);
        entity.setUserId(userId);
        entity.setCustomerId(customerId);
        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId))
                .thenReturn(Optional.of(entity));

        Invoice result = invoiceService.getInvoice(userId, customerId, invoiceId);

        assertEquals(invoiceId.toString(), result.id());
    }

    @Test
    void getInvoicesReturnsList() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        InvoiceEntity e1 = new InvoiceEntity();
        e1.setId(UUID.randomUUID());
        e1.setUserId(userId);
        e1.setCustomerId(customerId);
        InvoiceEntity e2 = new InvoiceEntity();
        e2.setId(UUID.randomUUID());
        e2.setUserId(userId);
        e2.setCustomerId(customerId);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(customerRepository.findByIdAndUserId(customerId, userId)).thenReturn(Optional.of(new CustomerEntity()));
        when(invoiceRepository.findAllByUserIdAndCustomerId(userId, customerId)).thenReturn(List.of(e1, e2));

        List<Invoice> result = invoiceService.getInvoices(userId, customerId);

        assertEquals(2, result.size());
    }
}
