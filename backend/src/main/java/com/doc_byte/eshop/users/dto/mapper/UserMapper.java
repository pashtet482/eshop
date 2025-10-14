package com.doc_byte.eshop.users.dto.mapper;

import com.doc_byte.eshop.users.dto.GetAllUsers;
import com.doc_byte.eshop.users.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public GetAllUsers toDto(@NotNull User user) {
        return new GetAllUsers(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getEmail(),
                user.getRole(),
                user.getLastUsernameChange()
        );
    }
}
