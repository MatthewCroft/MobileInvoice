package org.example.backend.user.service;

import org.example.backend.user.domain.User;
import org.example.backend.user.dto.CreateUserRequest;
import org.example.backend.user.entity.UserEntity;
import org.example.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void createUser() {
        CreateUserRequest request = new CreateUserRequest("email@example.com", "pass");
        UserEntity saved = new UserEntity();
        saved.setId(UUID.randomUUID());
        saved.setEmail("email@example.com");
        saved.setPasswordHash("pass");

        when(userRepository.save(any(UserEntity.class))).thenReturn(saved);

        User result = userService.createUser(request);

        assertEquals(saved.getId().toString(), result.id());
        assertEquals("email@example.com", result.email());
    }

    @Test
    void createUserDuplicateEmail() {
        CreateUserRequest request = new CreateUserRequest("email@example.com", "pass");
        when(userRepository.existsByEmail("email@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
    }

    @Test
    void deleteUser() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail("email@example.com");
        entity.setPasswordHash("pass");

        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        User result = userService.deleteUser(id);

        verify(userRepository).delete(entity);
        assertEquals(id.toString(), result.id());
    }

    @Test
    void getUserReturnsDomain() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail("email@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        User result = userService.getUser(id);

        assertEquals(id.toString(), result.id());
    }

    @Test
    void getUsersReturnsAllUsers() {
        UserEntity e1 = new UserEntity();
        e1.setId(UUID.randomUUID());
        e1.setEmail("a@example.com");
        UserEntity e2 = new UserEntity();
        e2.setId(UUID.randomUUID());
        e2.setEmail("b@example.com");
        when(userRepository.findAll()).thenReturn(List.of(e1, e2));

        List<User> result = userService.getUsers();

        assertEquals(2, result.size());
    }
}
