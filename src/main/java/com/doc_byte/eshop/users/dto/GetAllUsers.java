package com.doc_byte.eshop.users.dto;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * DTO for {@link com.doc_byte.eshop.users.model.User}
 */
public record GetAllUsers(
        String username,
        Instant createdAt,
        String email,
        Timestamp lastUsernameChange
) {}
