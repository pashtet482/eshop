package com.doc_byte.eshop.services;

import com.doc_byte.eshop.dto.CreateUserRequest;
import com.doc_byte.eshop.exceptions.UserNotFoundException;
import com.doc_byte.eshop.model.User;
import com.doc_byte.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean userExists(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean hasPermissionToChangeUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        String loggedInUsername = getCurrentUsername();
        if (!loggedInUsername.equals(username)) {
            return false;
        }

        LocalDateTime lastChange = user.getLastUsernameChange().toLocalDateTime();
        return lastChange == null || !lastChange.isAfter(LocalDateTime.now().minusDays(30));
    }


    public User createUser(@NotNull CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(String username, String password){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        if (!encoder.matches(password, user.getPassword())) return false;

        userRepository.delete(user);
        return true;
    }

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean changeUsername (String oldUsername, String newUsername){
        if (!userRepository.existsByUsername(oldUsername)) return false;
        if (userRepository.existsByUsername(newUsername)) return false;

        User user = userRepository.findByUsername(oldUsername).orElseThrow(() -> new UserNotFoundException(oldUsername));
        user.setUsername(newUsername);
        user.setLastUsernameChange(Timestamp.from(Instant.now()));
        userRepository.save(user);
        return true;
    }
}
