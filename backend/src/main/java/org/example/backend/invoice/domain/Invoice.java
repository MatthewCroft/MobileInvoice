package org.example.backend.invoice.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Invoice(
        String id,
        String invoiceNumber,
        LocalDate issueDate,
        LocalDate dueDate,
        String status,
        BigDecimal taxAmount,
        BigDecimal subtotal,
        BigDecimal total,
        String notes
) {
}
