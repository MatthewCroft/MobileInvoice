package org.example.backend.customer.controller;

import org.example.backend.customer.domain.Customer;
import org.example.backend.customer.dto.CreateCustomerRequest;
import org.example.backend.customer.dto.UpdateCustomerRequest;
import org.example.backend.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@PathVariable String userId,
                                   @Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(UUID.fromString(userId), request);
    }

    @GetMapping
    public List<Customer> getCustomers(@PathVariable String userId) {
        return customerService.getCustomers(UUID.fromString(userId));
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable String userId,
                                @PathVariable String customerId) {
        try {
            return customerService.getCustomer(UUID.fromString(userId), UUID.fromString(customerId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{customerId}")
    public Customer updateCustomer(@PathVariable String userId,
                                   @PathVariable String customerId,
                                   @Valid @RequestBody UpdateCustomerRequest request) {
        try {
            return customerService.updateCustomer(UUID.fromString(userId), UUID.fromString(customerId), request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{customerId}")
    public Customer deleteCustomer(@PathVariable String userId,
                                   @PathVariable String customerId) {
        try {
            return customerService.deleteCustomer(UUID.fromString(userId), UUID.fromString(customerId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
