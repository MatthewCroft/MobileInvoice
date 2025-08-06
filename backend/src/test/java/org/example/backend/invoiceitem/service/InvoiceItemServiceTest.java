package org.example.backend.invoiceitem.service;

import org.example.backend.invoice.entity.InvoiceEntity;
import org.example.backend.invoice.repository.InvoiceRepository;
import org.example.backend.invoiceitem.domain.InvoiceItem;
import org.example.backend.invoiceitem.dto.CreateInvoiceItemRequest;
import org.example.backend.invoiceitem.dto.UpdateInvoiceItemRequest;
import org.example.backend.invoiceitem.entity.InvoiceItemEntity;
import org.example.backend.invoiceitem.repository.InvoiceItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceItemServiceTest {

    private final InvoiceItemRepository invoiceItemRepository = mock(InvoiceItemRepository.class);
    private final InvoiceRepository invoiceRepository = mock(InvoiceRepository.class);
    private final InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository, invoiceRepository);

    @Test
    void createItemSavesAndReturnsDomain() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        CreateInvoiceItemRequest request = new CreateInvoiceItemRequest("desc", 2, BigDecimal.TEN);

        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId))
                .thenReturn(Optional.of(new InvoiceEntity()));

        InvoiceItemEntity saved = new InvoiceItemEntity();
        saved.setId(UUID.randomUUID());
        saved.setInvoiceId(invoiceId);
        saved.setDescription("desc");
        saved.setQuantity(2);
        saved.setUnitPrice(BigDecimal.TEN);
        saved.setTotalPrice(BigDecimal.valueOf(20));

        when(invoiceItemRepository.save(any(InvoiceItemEntity.class))).thenReturn(saved);

        InvoiceItem result = invoiceItemService.createItem(userId, customerId, invoiceId, request);

        ArgumentCaptor<InvoiceItemEntity> captor = ArgumentCaptor.forClass(InvoiceItemEntity.class);
        verify(invoiceItemRepository).save(captor.capture());
        assertEquals("desc", captor.getValue().getDescription());
        assertEquals(saved.getId().toString(), result.id());
    }

    @Test
    void updateItemUpdatesFields() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UpdateInvoiceItemRequest request = new UpdateInvoiceItemRequest("new", 3, BigDecimal.ONE);

        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId))
                .thenReturn(Optional.of(new InvoiceEntity()));

        InvoiceItemEntity entity = new InvoiceItemEntity();
        entity.setId(itemId);
        entity.setInvoiceId(invoiceId);
        entity.setDescription("old");
        entity.setQuantity(1);
        entity.setUnitPrice(BigDecimal.TEN);

        when(invoiceItemRepository.findByIdAndInvoiceId(itemId, invoiceId)).thenReturn(Optional.of(entity));
        when(invoiceItemRepository.save(entity)).thenReturn(entity);

        InvoiceItem result = invoiceItemService.updateItem(userId, customerId, invoiceId, itemId, request);

        assertEquals("new", entity.getDescription());
        verify(invoiceItemRepository).save(entity);
        assertEquals(itemId.toString(), result.id());
    }

    @Test
    void deleteItemDeletesEntity() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId))
                .thenReturn(Optional.of(new InvoiceEntity()));

        InvoiceItemEntity entity = new InvoiceItemEntity();
        entity.setId(itemId);
        entity.setInvoiceId(invoiceId);
        entity.setDescription("d");

        when(invoiceItemRepository.findByIdAndInvoiceId(itemId, invoiceId)).thenReturn(Optional.of(entity));

        InvoiceItem result = invoiceItemService.deleteItem(userId, customerId, invoiceId, itemId);

        verify(invoiceItemRepository).delete(entity);
        assertEquals(itemId.toString(), result.id());
    }

    @Test
    void getItemReturnsDomain() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId))
                .thenReturn(Optional.of(new InvoiceEntity()));
        InvoiceItemEntity entity = new InvoiceItemEntity();
        entity.setId(itemId);
        entity.setInvoiceId(invoiceId);
        when(invoiceItemRepository.findByIdAndInvoiceId(itemId, invoiceId)).thenReturn(Optional.of(entity));

        InvoiceItem result = invoiceItemService.getItem(userId, customerId, invoiceId, itemId);

        assertEquals(itemId.toString(), result.id());
    }

    @Test
    void getItemsReturnsList() {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        when(invoiceRepository.findByIdAndUserIdAndCustomerId(invoiceId, userId, customerId))
                .thenReturn(Optional.of(new InvoiceEntity()));
        InvoiceItemEntity e1 = new InvoiceItemEntity();
        e1.setId(UUID.randomUUID());
        e1.setInvoiceId(invoiceId);
        InvoiceItemEntity e2 = new InvoiceItemEntity();
        e2.setId(UUID.randomUUID());
        e2.setInvoiceId(invoiceId);
        when(invoiceItemRepository.findAllByInvoiceId(invoiceId)).thenReturn(List.of(e1, e2));

        List<InvoiceItem> result = invoiceItemService.getItems(userId, customerId, invoiceId);

        assertEquals(2, result.size());
    }
}
