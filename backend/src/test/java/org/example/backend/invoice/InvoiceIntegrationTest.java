package org.example.backend.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.customer.entity.CustomerEntity;
import org.example.backend.customer.repository.CustomerRepository;
import org.example.backend.invoice.dto.CreateInvoiceRequest;
import org.example.backend.invoice.dto.UpdateInvoiceRequest;
import org.example.backend.user.entity.UserEntity;
import org.example.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InvoiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUpdateDeleteInvoiceFlow() throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPasswordHash("pw");
        user = userRepository.save(user);
        UUID userId = user.getId();

        CustomerEntity customer = new CustomerEntity();
        customer.setUserId(userId);
        customer.setName("Cust");
        customer = customerRepository.save(customer);
        UUID customerId = customer.getId();

        CreateInvoiceRequest createRequest = new CreateInvoiceRequest("INV-1", LocalDate.now(), null, "unpaid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(11), "note");
        String createResponse = mockMvc.perform(post("/user/{userId}/customer/{customerId}/invoice", userId.toString(), customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        String invoiceId = objectMapper.readTree(createResponse).get("id").asText();

        UpdateInvoiceRequest updateRequest = new UpdateInvoiceRequest("INV-2", LocalDate.now(), null, "paid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(11), "note");
        mockMvc.perform(put("/user/{userId}/customer/{customerId}/invoice/{invoiceId}", userId.toString(), customerId.toString(), invoiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-2"));

        mockMvc.perform(delete("/user/{userId}/customer/{customerId}/invoice/{invoiceId}", userId.toString(), customerId.toString(), invoiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(invoiceId));
    }
}
