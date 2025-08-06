package org.example.backend.invoice.repository;

import org.example.backend.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
    Optional<InvoiceEntity> findByIdAndUserIdAndCustomerId(UUID id, UUID userId, UUID customerId);
    List<InvoiceEntity> findAllByUserIdAndCustomerId(UUID userId, UUID customerId);
}
