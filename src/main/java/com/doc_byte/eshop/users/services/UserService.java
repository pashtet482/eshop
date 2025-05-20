package com.doc_byte.eshop.users.services;

import com.doc_byte.eshop.users.model.User;
import com.doc_byte.eshop.users.dto.CreateUserRequest;
import com.doc_byte.eshop.exceptions.ConflictException;
import com.doc_byte.eshop.exceptions.UserNotFoundException;
import com.doc_byte.eshop.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean hasPermissionToChangeUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        Timestamp lastChange = user.getLastUsernameChange();
        return lastChange == null || lastChange.toLocalDateTime().isBefore(LocalDateTime.now().minusDays(30));
    }

    public void createUser(@NotNull CreateUserRequest request) {
        validateCreateUserRequest(request);

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new ConflictException("Старый пароль не совпадает");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ConflictException("Пароль неверный");
        }

        userRepository.delete(user);
    }

    public void changeUsername(String oldUsername, String newUsername) {
        if (userRepository.existsByUsername(newUsername)) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }

        User user = userRepository.findByUsername(oldUsername).orElseThrow(() -> new UserNotFoundException(oldUsername));

        user.setUsername(newUsername);
        user.setLastUsernameChange(Timestamp.from(Instant.now()));
        userRepository.save(user);
    }

    private void validateCreateUserRequest(@NotNull CreateUserRequest request) {
        if (request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("Введите имя");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Введите email");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Введите пароль");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
    }
}
