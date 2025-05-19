package com.doc_byte.eshop.services;

import com.doc_byte.eshop.exceptions.UserNotFoundException;
import com.doc_byte.eshop.model.User;
import com.doc_byte.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        List<String> usernames = userRepository.findAllUsernames();

        return usernames.contains(username);
    }

    public boolean hasPermissionToChangeUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!loggedInUsername.equals(username)) {
            return false;
        }

        LocalDateTime lastChange = user.getLastUsernameChange().toLocalDateTime();
        return lastChange == null || !lastChange.isAfter(LocalDateTime.now().minusDays(30));
    }


    @NotNull
    public User createUser(@NotNull User user) {
        user.setCreatedAt(Instant.now());
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        if (!encoder.matches(oldPassword, user.getPassword())) return false;

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

    public boolean changeUsername (String oldUsername, String newUsername){
        Optional<User> optionalUser = userRepository.findByUsername(oldUsername);

        if (optionalUser.isEmpty()) return false;

        List<String> usernames = userRepository.findAllUsernames();

        if(usernames.contains(newUsername)){
            return false;
        } else {
            User user = optionalUser.get();

            user.setUsername(newUsername);
            userRepository.save(user);
            return true;
        }
    }
}
