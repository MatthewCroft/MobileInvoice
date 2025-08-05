package org.example.backend.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.customer.dto.CreateCustomerRequest;
import org.example.backend.customer.dto.UpdateCustomerRequest;
import org.example.backend.user.entity.UserEntity;
import org.example.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUpdateDeleteCustomerFlow() throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPasswordHash("pw");
        user = userRepository.save(user);
        UUID userId = user.getId();

        CreateCustomerRequest createRequest = new CreateCustomerRequest("John", "123", "john@example.com", "Street");
        String createResponse = mockMvc.perform(post("/user/{userId}/customer", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        String customerId = objectMapper.readTree(createResponse).get("id").asText();

        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest("Jane", "456", "jane@example.com", "Road");
        mockMvc.perform(put("/user/{userId}/customer/{customerId}", userId.toString(), customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"));

        mockMvc.perform(delete("/user/{userId}/customer/{customerId}", userId.toString(), customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId));
    }
}
