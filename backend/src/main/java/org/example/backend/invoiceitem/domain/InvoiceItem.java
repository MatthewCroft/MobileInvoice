package org.example.backend.invoiceitem.domain;

import java.math.BigDecimal;

public record InvoiceItem(
        String id,
        String description,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
