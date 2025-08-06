package org.example.backend.invoiceitem.controller;

import org.example.backend.invoiceitem.domain.InvoiceItem;
import org.example.backend.invoiceitem.dto.CreateInvoiceItemRequest;
import org.example.backend.invoiceitem.dto.UpdateInvoiceItemRequest;
import org.example.backend.invoiceitem.service.InvoiceItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item")
public class InvoiceItemController {
    private final InvoiceItemService invoiceItemService;

    public InvoiceItemController(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceItem createItem(@PathVariable String userId,
                                  @PathVariable String customerId,
                                  @PathVariable String invoiceId,
                                  @Valid @RequestBody CreateInvoiceItemRequest request) {
        try {
            return invoiceItemService.createItem(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId), request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping
    public List<InvoiceItem> getItems(@PathVariable String userId,
                                      @PathVariable String customerId,
                                      @PathVariable String invoiceId) {
        try {
            return invoiceItemService.getItems(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{itemId}")
    public InvoiceItem getItem(@PathVariable String userId,
                               @PathVariable String customerId,
                               @PathVariable String invoiceId,
                               @PathVariable String itemId) {
        try {
            return invoiceItemService.getItem(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId), UUID.fromString(itemId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{itemId}")
    public InvoiceItem updateItem(@PathVariable String userId,
                                  @PathVariable String customerId,
                                  @PathVariable String invoiceId,
                                  @PathVariable String itemId,
                                  @Valid @RequestBody UpdateInvoiceItemRequest request) {
        try {
            return invoiceItemService.updateItem(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId), UUID.fromString(itemId), request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{itemId}")
    public InvoiceItem deleteItem(@PathVariable String userId,
                                  @PathVariable String customerId,
                                  @PathVariable String invoiceId,
                                  @PathVariable String itemId) {
        try {
            return invoiceItemService.deleteItem(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId), UUID.fromString(itemId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
