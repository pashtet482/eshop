package com.doc_byte.eshop.users.dto;

import com.doc_byte.eshop.users.model.User;

/**
 * DTO for {@link User}
 */

public record LoginDTO(String email, String password) {}
