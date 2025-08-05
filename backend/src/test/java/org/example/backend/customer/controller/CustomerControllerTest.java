package org.example.backend.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.customer.domain.Customer;
import org.example.backend.customer.dto.CreateCustomerRequest;
import org.example.backend.customer.dto.UpdateCustomerRequest;
import org.example.backend.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createCustomerReturnsCreatedCustomer() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateCustomerRequest request = new CreateCustomerRequest("John", "123", "j@example.com", "Street");
        Customer response = new Customer(UUID.randomUUID().toString(), "John", "123", "j@example.com", "Street");
        given(customerService.createCustomer(eq(userId), any(CreateCustomerRequest.class))).willReturn(response);

        mockMvc.perform(post("/user/{userId}/customer", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void updateCustomerReturnsUpdatedCustomer() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UpdateCustomerRequest request = new UpdateCustomerRequest("Jane", "456", "j2@example.com", "Road");
        Customer response = new Customer(customerId.toString(), "Jane", "456", "j2@example.com", "Road");
        given(customerService.updateCustomer(eq(userId), eq(customerId), any(UpdateCustomerRequest.class))).willReturn(response);

        mockMvc.perform(put("/user/{userId}/customer/{customerId}", userId.toString(), customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"));
    }

    @Test
    void deleteCustomerReturnsDeletedCustomer() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Customer response = new Customer(customerId.toString(), "Del", "1", "e", "a");
        given(customerService.deleteCustomer(userId, customerId)).willReturn(response);

        mockMvc.perform(delete("/user/{userId}/customer/{customerId}", userId.toString(), customerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId.toString()));
    }
}
