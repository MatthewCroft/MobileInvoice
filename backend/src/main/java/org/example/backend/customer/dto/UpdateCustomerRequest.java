package org.example.backend.customer.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(
        @NotBlank String name,
        String phone,
        String email,
        String address
) {
}
