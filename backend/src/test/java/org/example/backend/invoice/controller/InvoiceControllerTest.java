package org.example.backend.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.invoice.domain.Invoice;
import org.example.backend.invoice.dto.CreateInvoiceRequest;
import org.example.backend.invoice.dto.UpdateInvoiceRequest;
import org.example.backend.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void createInvoiceReturnsCreatedInvoice() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        CreateInvoiceRequest request = new CreateInvoiceRequest("INV-1", LocalDate.now(), null, "unpaid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(11), "note");
        Invoice response = new Invoice(UUID.randomUUID().toString(), "INV-1", LocalDate.now(), null, "unpaid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(11), "note");
        given(invoiceService.createInvoice(eq(userId), eq(customerId), any(CreateInvoiceRequest.class))).willReturn(response);

        mockMvc.perform(post("/user/{userId}/customer/{customerId}/invoice", userId.toString(), customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-1"));
    }

    @Test
    void updateInvoiceReturnsUpdatedInvoice() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UpdateInvoiceRequest request = new UpdateInvoiceRequest("INV-2", LocalDate.now(), null, "paid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(11), "note");
        Invoice response = new Invoice(invoiceId.toString(), "INV-2", LocalDate.now(), null, "paid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(11), "note");
        given(invoiceService.updateInvoice(eq(userId), eq(customerId), eq(invoiceId), any(UpdateInvoiceRequest.class))).willReturn(response);

        mockMvc.perform(put("/user/{userId}/customer/{customerId}/invoice/{invoiceId}", userId.toString(), customerId.toString(), invoiceId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-2"));
    }

    @Test
    void deleteInvoiceReturnsDeletedInvoice() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        Invoice response = new Invoice(invoiceId.toString(), "INV-3", LocalDate.now(), null, "unpaid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(11), "note");
        given(invoiceService.deleteInvoice(userId, customerId, invoiceId)).willReturn(response);

        mockMvc.perform(delete("/user/{userId}/customer/{customerId}/invoice/{invoiceId}", userId.toString(), customerId.toString(), invoiceId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(invoiceId.toString()));
    }

    @Test
    void getInvoiceReturnsInvoice() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        Invoice response = new Invoice(invoiceId.toString(), "INV", LocalDate.now(), null, "unpaid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN, "n");
        given(invoiceService.getInvoice(userId, customerId, invoiceId)).willReturn(response);

        mockMvc.perform(get("/user/{userId}/customer/{customerId}/invoice/{invoiceId}", userId.toString(), customerId.toString(), invoiceId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(invoiceId.toString()));
    }

    @Test
    void getInvoicesReturnsList() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Invoice i1 = new Invoice("1", "INV1", LocalDate.now(), null, "unpaid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN, "n");
        Invoice i2 = new Invoice("2", "INV2", LocalDate.now(), null, "unpaid", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN, "n");
        given(invoiceService.getInvoices(userId, customerId)).willReturn(List.of(i1, i2));

        mockMvc.perform(get("/user/{userId}/customer/{customerId}/invoice", userId.toString(), customerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }
}
