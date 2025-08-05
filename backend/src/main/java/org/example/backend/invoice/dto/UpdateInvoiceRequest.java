package org.example.backend.invoice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInvoiceRequest(
        @NotBlank String invoiceNumber,
        @NotNull LocalDate issueDate,
        LocalDate dueDate,
        @NotBlank String status,
        BigDecimal taxAmount,
        BigDecimal subtotal,
        BigDecimal total,
        String notes
) {
}
