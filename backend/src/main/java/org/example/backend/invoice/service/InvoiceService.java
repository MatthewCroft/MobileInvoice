package org.example.backend.invoice.service;

import org.example.backend.customer.repository.CustomerRepository;
import org.example.backend.invoice.domain.Invoice;
import org.example.backend.invoice.dto.CreateInvoiceRequest;
import org.example.backend.invoice.dto.UpdateInvoiceRequest;
import org.example.backend.invoice.entity.InvoiceEntity;
import org.example.backend.invoice.repository.InvoiceRepository;
import org.example.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          UserRepository userRepository,
                          CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public Invoice createInvoice(UUID userId, UUID customerId, CreateInvoiceRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        customerRepository.findByIdAndUserId(customerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        InvoiceEntity entity = new InvoiceEntity();
        entity.setUserId(userId);
        entity.setCustomerId(customerId);
        entity.setInvoiceNumber(request.invoiceNumber());
        entity.setIssueDate(request.issueDate());
        entity.setDueDate(request.dueDate());
        entity.setStatus(request.status());
        entity.setTaxAmount(request.taxAmount());
        entity.setSubtotal(request.subtotal());
        entity.setTotal(request.total());
        entity.setNotes(request.notes());
        InvoiceEntity saved = invoiceRepository.save(entity);
        return toDomain(saved);
    }

    public Invoice updateInvoice(UUID userId, UUID customerId, UUID invoiceId, UpdateInvoiceRequest request) {
        InvoiceEntity entity = invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        entity.setInvoiceNumber(request.invoiceNumber());
        entity.setIssueDate(request.issueDate());
        entity.setDueDate(request.dueDate());
        entity.setStatus(request.status());
        entity.setTaxAmount(request.taxAmount());
        entity.setSubtotal(request.subtotal());
        entity.setTotal(request.total());
        entity.setNotes(request.notes());
        InvoiceEntity saved = invoiceRepository.save(entity);
        return toDomain(saved);
    }

    public Invoice deleteInvoice(UUID userId, UUID customerId, UUID invoiceId) {
        InvoiceEntity entity = invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoiceRepository.delete(entity);
        return toDomain(entity);
    }

    private Invoice toDomain(InvoiceEntity entity) {
        return new Invoice(
                entity.getId().toString(),
                entity.getInvoiceNumber(),
                entity.getIssueDate(),
                entity.getDueDate(),
                entity.getStatus(),
                entity.getTaxAmount(),
                entity.getSubtotal(),
                entity.getTotal(),
                entity.getNotes()
        );
    }
}
