package org.example.backend.invoiceitem.repository;

import org.example.backend.invoiceitem.entity.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, UUID> {
    Optional<InvoiceItemEntity> findByIdAndInvoiceId(UUID id, UUID invoiceId);
}
