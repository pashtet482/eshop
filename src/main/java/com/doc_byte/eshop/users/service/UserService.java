package com.doc_byte.eshop.users.service;

import com.doc_byte.eshop.users.dto.GetAllUsers;
import com.doc_byte.eshop.users.dto.LoginDTO;
import com.doc_byte.eshop.users.dto.mapper.UserMapper;
import com.doc_byte.eshop.users.model.User;
import com.doc_byte.eshop.users.dto.CreateUserRequest;
import com.doc_byte.eshop.exceptions.ConflictException;
import com.doc_byte.eshop.exceptions.NotFoundException;
import com.doc_byte.eshop.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javafx.util.Pair;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;
    public static final String USER_NOT_FOUND_TEMPLATE = "Пользователь %s не найден";

    public List<GetAllUsers> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public Pair<Integer, User> getDaysUntilUsernameChangeAllowed(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, username)));

        Timestamp lastChange = user.getLastUsernameChange();
        if (lastChange == null) return new Pair<>(0, user);

        LocalDateTime nextAllowedDate = lastChange.toLocalDateTime().plusDays(30);
        long daysLeft = Duration.between(LocalDateTime.now(), nextAllowedDate).toDays();

        return new Pair<>((int) Math.max(0, daysLeft), user);
    }


    public User createUser(@NotNull CreateUserRequest request) {
        validateCreateUserRequest(request);

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
        return user;
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, username)));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new ConflictException("Старый пароль не совпадает");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, username)));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ConflictException("Пароль неверный");
        }

        userRepository.delete(user);
    }

    public void changeUsername(String oldUsername, String newUsername) {
        if (userRepository.existsByUsername(newUsername)) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }

        User user = userRepository.findByUsername(oldUsername).orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, oldUsername)));

        user.setUsername(newUsername);
        user.setLastUsernameChange(Timestamp.from(Instant.now()));
        userRepository.save(user);
    }

    private void validateCreateUserRequest(@NotNull CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
    }

    public User login(@NotNull LoginDTO loginDTO){
        User user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new NotFoundException("Аккаунт с таким email не найден"));

        String password = loginDTO.password();
        if (encoder.matches(password, user.getPassword())){
            return user;
        }
        return null;
    }
}
