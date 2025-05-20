package com.doc_byte.eshop.users.dto;


import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
        @NotBlank String username,
        @NotBlank String oldPassword,
        @NotBlank String newPassword) {
}
