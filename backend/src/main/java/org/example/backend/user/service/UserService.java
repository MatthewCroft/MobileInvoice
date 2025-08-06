package org.example.backend.user.service;

import org.example.backend.user.domain.User;
import org.example.backend.user.dto.CreateUserRequest;
import org.example.backend.user.entity.UserEntity;
import org.example.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("User already exists");
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(request.email());
        entity.setPasswordHash(request.password());
        UserEntity saved = userRepository.save(entity);
        return new User(saved.getId().toString(), saved.getEmail());
    }

    public User deleteUser(UUID id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(entity);
        return new User(entity.getId().toString(), entity.getEmail());
    }

    public User getUser(UUID id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new User(entity.getId().toString(), entity.getEmail());
    }

    public List<User> getUsers() {
        return userRepository.findAll().stream()
                .map(e -> new User(e.getId().toString(), e.getEmail()))
                .toList();
    }
}
