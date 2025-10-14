package com.doc_byte.eshop.users.dto;


import jakarta.validation.constraints.NotBlank;
/**
 * DTO for {@link com.doc_byte.eshop.users.model.User}
 */
public record PasswordChangeRequest(
        @NotBlank String username,
        @NotBlank String oldPassword,
        @NotBlank String newPassword) {
}
