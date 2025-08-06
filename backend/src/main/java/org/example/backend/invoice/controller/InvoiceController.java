package org.example.backend.invoice.controller;

import org.example.backend.invoice.domain.Invoice;
import org.example.backend.invoice.dto.CreateInvoiceRequest;
import org.example.backend.invoice.dto.UpdateInvoiceRequest;
import org.example.backend.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/customer/{customerId}/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice createInvoice(@PathVariable String userId,
                                 @PathVariable String customerId,
                                 @Valid @RequestBody CreateInvoiceRequest request) {
        try {
            return invoiceService.createInvoice(UUID.fromString(userId), UUID.fromString(customerId), request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping
    public List<Invoice> getInvoices(@PathVariable String userId,
                                     @PathVariable String customerId) {
        try {
            return invoiceService.getInvoices(UUID.fromString(userId), UUID.fromString(customerId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoice(@PathVariable String userId,
                              @PathVariable String customerId,
                              @PathVariable String invoiceId) {
        try {
            return invoiceService.getInvoice(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{invoiceId}")
    public Invoice updateInvoice(@PathVariable String userId,
                                 @PathVariable String customerId,
                                 @PathVariable String invoiceId,
                                 @Valid @RequestBody UpdateInvoiceRequest request) {
        try {
            return invoiceService.updateInvoice(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId), request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{invoiceId}")
    public Invoice deleteInvoice(@PathVariable String userId,
                                 @PathVariable String customerId,
                                 @PathVariable String invoiceId) {
        try {
            return invoiceService.deleteInvoice(UUID.fromString(userId), UUID.fromString(customerId), UUID.fromString(invoiceId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
