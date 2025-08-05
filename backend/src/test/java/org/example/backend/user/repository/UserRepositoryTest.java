package org.example.backend.user.repository;

import org.example.backend.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndDeleteUser() {
        UserEntity entity = new UserEntity();
        entity.setEmail("test@example.com");
        entity.setPasswordHash("secret");
        UserEntity saved = userRepository.save(entity);

        Optional<UserEntity> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertTrue(userRepository.existsByEmail("test@example.com"));

        userRepository.deleteById(saved.getId());
        assertFalse(userRepository.findById(saved.getId()).isPresent());
        assertFalse(userRepository.existsByEmail("test@example.com"));
    }
}
