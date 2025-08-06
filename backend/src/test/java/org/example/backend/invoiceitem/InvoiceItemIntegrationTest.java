package org.example.backend.invoiceitem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.customer.entity.CustomerEntity;
import org.example.backend.customer.repository.CustomerRepository;
import org.example.backend.invoice.entity.InvoiceEntity;
import org.example.backend.invoice.repository.InvoiceRepository;
import org.example.backend.invoiceitem.dto.CreateInvoiceItemRequest;
import org.example.backend.invoiceitem.dto.UpdateInvoiceItemRequest;
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
class InvoiceItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUpdateDeleteItemFlow() throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("item@example.com");
        user.setPasswordHash("pw");
        user = userRepository.save(user);
        UUID userId = user.getId();

        CustomerEntity customer = new CustomerEntity();
        customer.setUserId(userId);
        customer.setName("CustI");
        customer = customerRepository.save(customer);
        UUID customerId = customer.getId();

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setUserId(userId);
        invoice.setCustomerId(customerId);
        invoice.setInvoiceNumber("INV-ITEM-1");
        invoice.setIssueDate(LocalDate.now());
        invoice.setStatus("unpaid");
        invoice = invoiceRepository.save(invoice);
        UUID invoiceId = invoice.getId();

        CreateInvoiceItemRequest createRequest = new CreateInvoiceItemRequest("desc", 2, BigDecimal.TEN);
        String createResponse = mockMvc.perform(post("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item", userId.toString(), customerId.toString(), invoiceId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        String itemId = objectMapper.readTree(createResponse).get("id").asText();

        UpdateInvoiceItemRequest updateRequest = new UpdateInvoiceItemRequest("new", 3, BigDecimal.ONE);
        mockMvc.perform(put("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item/{itemId}", userId.toString(), customerId.toString(), invoiceId.toString(), itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("new"));

        mockMvc.perform(delete("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item/{itemId}", userId.toString(), customerId.toString(), invoiceId.toString(), itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));
    }
}
