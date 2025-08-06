package org.example.backend.invoiceitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.invoiceitem.domain.InvoiceItem;
import org.example.backend.invoiceitem.dto.CreateInvoiceItemRequest;
import org.example.backend.invoiceitem.dto.UpdateInvoiceItemRequest;
import org.example.backend.invoiceitem.service.InvoiceItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceItemController.class)
class InvoiceItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceItemService invoiceItemService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void createItemReturnsCreatedItem() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        CreateInvoiceItemRequest request = new CreateInvoiceItemRequest("desc", 2, BigDecimal.TEN);
        InvoiceItem response = new InvoiceItem(UUID.randomUUID().toString(), "desc", 2, BigDecimal.TEN, BigDecimal.valueOf(20));
        given(invoiceItemService.createItem(eq(userId), eq(customerId), eq(invoiceId), any(CreateInvoiceItemRequest.class))).willReturn(response);

        mockMvc.perform(post("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item", userId.toString(), customerId.toString(), invoiceId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("desc"));
    }

    @Test
    void updateItemReturnsUpdatedItem() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UpdateInvoiceItemRequest request = new UpdateInvoiceItemRequest("new", 3, BigDecimal.ONE);
        InvoiceItem response = new InvoiceItem(itemId.toString(), "new", 3, BigDecimal.ONE, BigDecimal.valueOf(3));
        given(invoiceItemService.updateItem(eq(userId), eq(customerId), eq(invoiceId), eq(itemId), any(UpdateInvoiceItemRequest.class))).willReturn(response);

        mockMvc.perform(put("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item/{itemId}",
                        userId.toString(), customerId.toString(), invoiceId.toString(), itemId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("new"));
    }

    @Test
    void deleteItemReturnsDeletedItem() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        InvoiceItem response = new InvoiceItem(itemId.toString(), "d", 1, BigDecimal.ONE, BigDecimal.ONE);
        given(invoiceItemService.deleteItem(userId, customerId, invoiceId, itemId)).willReturn(response);

        mockMvc.perform(delete("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item/{itemId}",
                        userId.toString(), customerId.toString(), invoiceId.toString(), itemId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId.toString()));
    }

    @Test
    void getItemReturnsItem() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        InvoiceItem response = new InvoiceItem(itemId.toString(), "d", 1, BigDecimal.ONE, BigDecimal.ONE);
        given(invoiceItemService.getItem(userId, customerId, invoiceId, itemId)).willReturn(response);

        mockMvc.perform(get("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item/{itemId}",
                        userId.toString(), customerId.toString(), invoiceId.toString(), itemId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId.toString()));
    }

    @Test
    void getItemsReturnsList() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        InvoiceItem i1 = new InvoiceItem("1", "a", 1, BigDecimal.ONE, BigDecimal.ONE);
        InvoiceItem i2 = new InvoiceItem("2", "b", 1, BigDecimal.ONE, BigDecimal.ONE);
        given(invoiceItemService.getItems(userId, customerId, invoiceId)).willReturn(List.of(i1, i2));

        mockMvc.perform(get("/user/{userId}/customer/{customerId}/invoice/{invoiceId}/item", userId.toString(), customerId.toString(), invoiceId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }
}
