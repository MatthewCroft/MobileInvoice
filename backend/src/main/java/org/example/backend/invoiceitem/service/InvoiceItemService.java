package org.example.backend.invoiceitem.service;

import org.example.backend.invoice.repository.InvoiceRepository;
import org.example.backend.invoiceitem.domain.InvoiceItem;
import org.example.backend.invoiceitem.dto.CreateInvoiceItemRequest;
import org.example.backend.invoiceitem.dto.UpdateInvoiceItemRequest;
import org.example.backend.invoiceitem.entity.InvoiceItemEntity;
import org.example.backend.invoiceitem.repository.InvoiceItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceItemService {
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceItemService(InvoiceItemRepository invoiceItemRepository,
                              InvoiceRepository invoiceRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceItem createItem(UUID userId, UUID customerId, UUID invoiceId, CreateInvoiceItemRequest request) {
        invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        InvoiceItemEntity entity = new InvoiceItemEntity();
        entity.setInvoiceId(invoiceId);
        entity.setDescription(request.description());
        entity.setQuantity(request.quantity());
        entity.setUnitPrice(request.unitPrice());
        entity.setTotalPrice(request.unitPrice().multiply(BigDecimal.valueOf(request.quantity())));
        InvoiceItemEntity saved = invoiceItemRepository.save(entity);
        return toDomain(saved);
    }

    public InvoiceItem updateItem(UUID userId, UUID customerId, UUID invoiceId, UUID itemId, UpdateInvoiceItemRequest request) {
        invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        InvoiceItemEntity entity = invoiceItemRepository.findByIdAndInvoiceId(itemId, invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        entity.setDescription(request.description());
        entity.setQuantity(request.quantity());
        entity.setUnitPrice(request.unitPrice());
        entity.setTotalPrice(request.unitPrice().multiply(BigDecimal.valueOf(request.quantity())));
        InvoiceItemEntity saved = invoiceItemRepository.save(entity);
        return toDomain(saved);
    }

    public InvoiceItem deleteItem(UUID userId, UUID customerId, UUID invoiceId, UUID itemId) {
        invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        InvoiceItemEntity entity = invoiceItemRepository.findByIdAndInvoiceId(itemId, invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        invoiceItemRepository.delete(entity);
        return toDomain(entity);
    }

    public InvoiceItem getItem(UUID userId, UUID customerId, UUID invoiceId, UUID itemId) {
        invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        InvoiceItemEntity entity = invoiceItemRepository.findByIdAndInvoiceId(itemId, invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        return toDomain(entity);
    }

    public List<InvoiceItem> getItems(UUID userId, UUID customerId, UUID invoiceId) {
        invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        return invoiceItemRepository.findAllByInvoiceId(invoiceId).stream()
                .map(this::toDomain)
                .toList();
    }

    private InvoiceItem toDomain(InvoiceItemEntity entity) {
        return new InvoiceItem(
                entity.getId().toString(),
                entity.getDescription(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getTotalPrice()
        );
    }
}
