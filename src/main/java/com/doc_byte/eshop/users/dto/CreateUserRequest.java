package com.doc_byte.eshop.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
/**
 * DTO for {@link com.doc_byte.eshop.users.model.User}
 */
public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank String password
) {}
