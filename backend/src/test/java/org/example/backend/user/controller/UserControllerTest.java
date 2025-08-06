package org.example.backend.user.controller;

import org.example.backend.user.domain.User;
import org.example.backend.user.dto.CreateUserRequest;
import org.example.backend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUser() throws Exception {
        User user = new User("1", "email@example.com");
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"email@example.com\",\"password\":\"pass\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@example.com"));
    }

    @Test
    void createUserValidationError() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserDuplicateEmail() throws Exception {
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(new IllegalArgumentException("User already exists"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@example.com\",\"password\":\"pass\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User(id.toString(), "email@example.com");
        when(userService.deleteUser(eq(id))).thenReturn(user);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User(id.toString(), "e@x.com");
        when(userService.getUser(id)).thenReturn(user);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getUsers() throws Exception {
        User u1 = new User("1", "a@b.com");
        User u2 = new User("2", "c@d.com");
        when(userService.getUsers()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }
}
